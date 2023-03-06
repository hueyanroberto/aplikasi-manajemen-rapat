package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityMeetingBinding
import ac.id.ubaya.aplikasimanajemenrapat.databinding.AlertJoinMeetngBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.UserViewModel
import ac.id.ubaya.aplikasimanajemenrapat.ui.login.LoginActivity
import ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda.MeetingAddAgendaActivity
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertDateFormat
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MeetingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMeetingBinding
    private val userViewModel: UserViewModel by viewModels()
    private val meetingViewModel: MeetingViewModel by viewModels()

    private lateinit var user: User
    private lateinit var meeting: Meeting
    private var meetingId: Int = -1
    private var selectedTab = 0

    companion object {
        private val TAB_TITLES = intArrayOf(R.string.tab_detail, R.string.tab_agenda, R.string.tab_participant)
        const val EXTRA_MEETING_ID = "extra_meeting_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabMeeting.hide()
        binding.buttonStartMeeting.visibility = View.GONE
        binding.imageMeetingBack.setOnClickListener { finish() }

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
                if (meeting.userRole == 1) {
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
                    if (meetingResource.data != null) {
                        meeting = meetingResource.data

                        val startTime = convertDateFormat(meeting.startTime)
                        val endTime = convertDateFormat(meeting.endTime)
                        val endSplit = endTime.split(" ")
                        val time = "$startTime - ${endSplit[3]}"

                        binding.textMeetingDetailTitle.text = meeting.title
                        binding.textMeetingDetailLocation.text = meeting.location
                        binding.textMeetingDetailTime.text = time

                        selectedTab = binding.tabMeeting.selectedTabPosition
                        initTabLayout(meeting)
                        setUpButtonMeeting(meeting)

                        binding.viewPagerMeeting.currentItem = selectedTab
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

    private fun setUpButtonMeeting(meeting: Meeting) {
        when (meeting.userRole) {
            1 -> {
                when (meeting.status) {
                    0 -> {
                        binding.buttonStartMeeting.text = resources.getString(R.string.start_meeting)
                        binding.buttonStartMeeting.setOnClickListener {
                            AlertDialog.Builder(this)
                                .setMessage(resources.getString(R.string.start_meeting_question))
                                .setPositiveButton(resources.getString(R.string.start)) {_, _ ->
                                    startMeeting(user.token.toString(), meeting.id)
                                }
                                .setNegativeButton(resources.getString(R.string.cancel), null)
                                .show()
                        }
                    }
                    1 -> {
                        binding.buttonStartMeeting.text = resources.getString(R.string.end_meeting)
                        binding.buttonStartMeeting.setOnClickListener {
                            AlertDialog.Builder(this)
                                .setMessage(resources.getString(R.string.end_meeting_question))
                                .setPositiveButton(resources.getString(R.string.end)) {_, _ ->
                                    endMeeting(user.token.toString(), meeting.id)
                                }
                                .setNegativeButton(resources.getString(R.string.cancel), null)
                                .show()
                        }
                    }
                    else -> {
                        binding.buttonStartMeeting.text = resources.getString(R.string.meeting_ended)
                        binding.buttonStartMeeting.setBackgroundResource(R.drawable.button_disable)
                        binding.buttonStartMeeting.setOnClickListener(null)
                    }
                }
                binding.buttonStartMeeting.visibility = View.VISIBLE

                binding.imageMeetingMore.visibility = View.VISIBLE
                binding.imageMeetingMore.setOnClickListener {
                    val popupMenu = PopupMenu(this, it)
                    popupMenu.inflate(R.menu.menu_show_code)
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.item_show_code_organization -> {
                                AlertDialog.Builder(this)
                                    .setMessage(resources.getString(R.string.meeting_code_data, meeting.code))
                                    .setPositiveButton(resources.getString(R.string.copy_to_clipboard)) { _, _ ->
                                        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Code", meeting.code)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(this, resources.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
                                    }
                                    .show()
                            }
                        }
                        true
                    }
                    popupMenu.show()
                }
            }
            2 -> {
                if (meeting.status == 0 || meeting.status == 1) {
                    if (meeting.userStatus == 0) {
                        binding.buttonStartMeeting.text = resources.getString(R.string.join_meeting)
                        binding.buttonStartMeeting.setOnClickListener {
                            val builder = AlertDialog.Builder(this)
                            val alertBinding = AlertJoinMeetngBinding.inflate(LayoutInflater.from(this))

                            with(builder) {
                                setView(alertBinding.root)
                                setCancelable(false)
                            }
                            val alertDialog = builder.create()

                            alertBinding.buttonJoinMeetingCancel.setOnClickListener { alertDialog.dismiss() }
                            alertBinding.buttonJoinMeeting.setOnClickListener {
                                val code = alertBinding.editJoinMeetingCode.text.toString().trim()
                                joinMeeting(user.token.toString(), meeting.id, code)
                                alertDialog.dismiss()
                            }

                            alertDialog.show()
                        }
                    } else if (meeting.userStatus == 1){
                        binding.buttonStartMeeting.text = resources.getString(R.string.meeting_joined)
                        binding.buttonStartMeeting.setBackgroundResource(R.drawable.button_disable)
                        binding.buttonStartMeeting.setOnClickListener(null)
                    }
                    binding.buttonStartMeeting.visibility = View.VISIBLE
                } else {
                    binding.buttonStartMeeting.text = resources.getString(R.string.meeting_ended)
                    binding.buttonStartMeeting.setBackgroundResource(R.drawable.button_disable)
                    binding.buttonStartMeeting.setOnClickListener(null)
                }
            }
        }
    }

    private fun startMeeting(token: String, meetingId: Int) {
        lifecycleScope.launch {
            meetingViewModel.startMeeting(token, meetingId, Date()).collect { meetingResource ->
                when (meetingResource) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val meetingGet = meetingResource.data
                        meetingGet?.let {
                            meeting.status = it.status
                            setUpButtonMeeting(meeting)
                            Toast.makeText(
                                this@MeetingActivity,
                                resources.getString(R.string.meeting_started),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    is Resource.Error -> {
                        Snackbar.make(binding.viewPagerMeeting, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .show()
                    }
                }
            }
        }
    }

    private fun joinMeeting(token: String, meetingId: Int, meetingCode: String) {
        lifecycleScope.launch {
            meetingViewModel.joinMeeting(token, meetingId, meetingCode, Date()).collect { meetingResource ->
                when (meetingResource) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val meetingGet = meetingResource.data
                        meetingGet?.let {
                            meeting.userStatus = it.userStatus
                            setUpButtonMeeting(meeting)
                            Toast.makeText(
                                this@MeetingActivity,
                                resources.getString(R.string.meeting_joined),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    is Resource.Error -> {
                        if (meetingResource.message == "wrong code") {
                            AlertDialog.Builder(this@MeetingActivity)
                                .setMessage(resources.getString(R.string.wrong_meeting_code))
                                .setPositiveButton(resources.getString(R.string.ok), null)
                                .show()
                        } else {
                            Snackbar.make(binding.viewPagerMeeting, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                                .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                                .setTextColor(resources.getColor(R.color.white, theme))
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun endMeeting(token: String, meetingId: Int) {
        lifecycleScope.launch {
            meetingViewModel.endMeeting(token, meetingId, Date()).collect { meetingResource ->
                when (meetingResource) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val meetingGet = meetingResource.data
                        meetingGet?.let {
                            meeting.status = it.status
                            setUpButtonMeeting(meeting)
                            Toast.makeText(
                                this@MeetingActivity,
                                resources.getString(R.string.meeting_ended),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    is Resource.Error -> {
                        Snackbar.make(binding.viewPagerMeeting, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .show()
                    }
                }
            }
        }
    }
}