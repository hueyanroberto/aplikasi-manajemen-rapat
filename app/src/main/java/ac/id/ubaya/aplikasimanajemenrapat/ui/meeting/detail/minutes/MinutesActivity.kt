package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.minutes

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Agenda
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityMinutesBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MinutesActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MEETING_ID = "extra_meeting_id"
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_MEETING = "extra_meeting"
    }

    private lateinit var binding: ActivityMinutesBinding
    private lateinit var listAgenda: List<Agenda>
    private val viewModel: MinutesViewModel by viewModels()
    private var meeting: Meeting? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinutesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val meetingId = intent.getIntExtra(EXTRA_MEETING_ID, -1)
        val token = intent.getStringExtra(EXTRA_TOKEN).toString()

        meeting = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_MEETING, Meeting::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_MEETING)
        }

        binding.textMinutesMeetingTitle.text = meeting?.title

        getMeeting(token, meetingId)
        getMinutes(token, meetingId)
    }

    private fun getMinutes(token: String, meetingId: Int) {
        lifecycleScope.launch {
            viewModel.getMinutes(token, meetingId).collect { agendaResource ->
                when (agendaResource) {
                    is Resource.Loading -> {
                        binding.progressBarMinutes.visibility = View.VISIBLE
                        binding.imageMinutesDownload.setOnClickListener(null)
                    }
                    is Resource.Success -> {
                        binding.progressBarMinutes.visibility = View.GONE
                        val listAgenda = agendaResource.data
                        listAgenda?.let {
                            val adapter = MinutesAdapter(this@MinutesActivity, it)
                            this@MinutesActivity.listAgenda = it
                            binding.expandableMinutes.setAdapter(adapter)
//
//                            binding.imageMinutesDownload.visibility = View.GONE
//                            binding.imageMinutesDownload.setOnClickListener { _ ->
//                                Toast.makeText(this@MinutesActivity, "Coming Soon", Toast.LENGTH_SHORT).show()
//                            }
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBarMinutes.visibility = View.GONE
                        Snackbar.make(binding.expandableMinutes, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .setAction(resources.getString(R.string.refresh)) {
                                getMinutes(token, meetingId)
                            }
                            .show()
                    }
                }
            }
        }
    }

    private fun getMeeting(token: String, meetingId: Int) {
        lifecycleScope.launch {
            viewModel.getMeetingDetail(token, meetingId).collect { meetingResource ->
                when (meetingResource) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val meeting = meetingResource.data
                        meeting?.let {meetingGet ->
                            val realStart = meetingGet.realStart?.let { convertDateFormat(it) }
                            val realEnd = meetingGet.realEnd?.let { convertDateFormat(it) }
                            val endSplit = realEnd?.split(" ")
                            val time = "$realStart - ${endSplit?.get(3)}"
                            binding.textMinutesMeetingRealtime.text = time
                            binding.textMinutesMeetingTitle.text = meetingGet.title
                        }
                    }
                    is Resource.Error -> {}
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}