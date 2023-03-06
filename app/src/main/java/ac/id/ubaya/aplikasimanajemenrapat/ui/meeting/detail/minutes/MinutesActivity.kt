package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.minutes

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityMinutesBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MinutesActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MEETING_ID = "extra_meeting_id"
        const val EXTRA_TOKEN = "extra_token"
    }

    private lateinit var binding: ActivityMinutesBinding
    private val viewModel: MinutesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinutesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val meetingId = intent.getIntExtra(EXTRA_MEETING_ID, -1)
        val token = intent.getStringExtra(EXTRA_TOKEN).toString()

        binding.imageMinutesBack.setOnClickListener { finish() }

        getMinutes(token, meetingId)
    }

    private fun getMinutes(token: String, meetingId: Int) {
        lifecycleScope.launch {
            viewModel.getMinutes(token, meetingId).collect { agendaResource ->
                when (agendaResource) {
                    is Resource.Loading -> {
                        binding.progressBarMinutes.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarMinutes.visibility = View.GONE
                        val listAgenda = agendaResource.data
                        listAgenda?.let {
                            val adapter = MinutesAdapter(this@MinutesActivity, it)
                            binding.expandableMinutes.setAdapter(adapter)

                            binding.imageMinutesDownload.visibility = View.VISIBLE
                            binding.imageMinutesDownload.setOnClickListener {
                                Toast.makeText(this@MinutesActivity, "Coming Soon", Toast.LENGTH_SHORT).show()
                            }
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
}