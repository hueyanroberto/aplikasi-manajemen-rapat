package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityMeetingBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.UserViewModel
import ac.id.ubaya.aplikasimanajemenrapat.ui.login.LoginActivity
import ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda.MeetingAddAgendaActivity
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertDateFormat
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeetingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMeetingBinding
    private val userViewModel: UserViewModel by viewModels()
    private val meetingViewModel: MeetingViewModel by viewModels()

    private lateinit var user: User
    private var meetingId: Int = -1

    companion object {
        private val TAB_TITLES = intArrayOf(R.string.tab_detail, R.string.tab_agenda, R.string.tab_participant)
        const val EXTRA_MEETING_ID = "extra_meeting_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fabMeeting.hide()

        init()
    }

    private fun init() {
        meetingId = intent.getIntExtra(EXTRA_MEETING_ID, -1)
        userViewModel.isUserGet.observe(this) {
            if (it) {
                meetingViewModel.getMeetingDetail(user.token.toString(), meetingId)
                observeMeetingDetail(user.token.toString(), meetingId)
            }
        }

        userViewModel.getUser().observe(this) { user ->
            if (user.id != -1) {
                this.user = user
                userViewModel.changeGetUserStatus()
            } else {
                Toast.makeText(this, resources.getString(R.string.user_not_login_error), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
        }
    }

    private fun initTabLayout(meeting: Meeting) {
        val adapter = MeetingSectionPagerAdapter(this, meeting, user.token.toString())
        binding.viewPagerMeeting.adapter = adapter
        TabLayoutMediator(binding.tabMeeting, binding.viewPagerMeeting) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.tabMeeting.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (meeting.userStatus == 1) {
                    when (binding.tabMeeting.selectedTabPosition) {
                        0 -> {
                            binding.fabMeeting.hide()
                        }
                        1 -> {
                            binding.fabMeeting.setImageResource(R.drawable.baseline_add_24)
                            binding.fabMeeting.show()

                            binding.fabMeeting.setOnClickListener {
                                val intent = Intent(this@MeetingActivity, MeetingAddAgendaActivity::class.java)
                                intent.putExtra(MeetingAddAgendaActivity.EXTRA_TOKEN, user.token.toString())
                                intent.putExtra(MeetingAddAgendaActivity.EXTRA_MEETING_ID, meetingId)
                                startActivity(intent)
                            }
                        }
                        2 -> {
                            binding.fabMeeting.setImageResource(R.drawable.baseline_edit_24)
                            binding.fabMeeting.show()

                            binding.fabMeeting.setOnClickListener(null)
                        }
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) { }
            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })
    }

    private fun observeMeetingDetail(token: String, meetingId: Int) {
        meetingViewModel.getMeetingDetail(token, meetingId).observe(this) { meetingResource ->
            when (meetingResource) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    val meeting = meetingResource.data
                    if (meeting != null) {
                        val startTime = convertDateFormat(meeting.startTime)
                        val endTime = convertDateFormat(meeting.endTime)
                        val endSplit = endTime.split(" ")
                        val time = "$startTime - ${endSplit[3]}"

                        binding.textMeetingDetailTitle.text = meeting.title
                        binding.textMeetingDetailLocation.text = meeting.location
                        binding.textMeetingDetailTime.text = time

                        initTabLayout(meeting)
                    }
                }
                is Resource.Error -> {
                    Snackbar.make(binding.viewPagerMeeting, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                        .setTextColor(resources.getColor(R.color.white, theme))
                        .setAction(resources.getString(R.string.refresh)) {
                            meetingViewModel.getMeetingDetail(user.token.toString(), meetingId)
                        }
                        .show()
                }
            }
        }
    }
}