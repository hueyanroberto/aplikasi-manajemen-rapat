package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Agenda
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityDetailAgendaBinding
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailAgendaActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_AGENDA = "extra_agenda"
        const val EXTRA_TOKEN = "extra_token"
    }

    private lateinit var binding: ActivityDetailAgendaBinding
    private val viewModel: DetailAgendaViewModel by viewModels()

    private var agenda: Agenda? = null
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAgendaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        agenda = if (Build.VERSION.SDK_INT > 33) {
            intent.getParcelableExtra(EXTRA_AGENDA, Agenda::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_AGENDA)
        }

        token = intent.getStringExtra(EXTRA_TOKEN).toString()

        binding.textAgendaTitle.text = agenda?.task.toString()
        agenda?.id?.let { getListSuggestion(token, it) }
    }

    private fun getListSuggestion(token: String, agendaId: Int) {
        binding.recyclerSuggestions.layoutManager = LinearLayoutManager(this)
        viewModel.getListSuggestion(token, agendaId).observe(this) { suggestionResource ->
            when (suggestionResource) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val listSuggestion = suggestionResource.data
                    binding.recyclerSuggestions.adapter = listSuggestion?.let {
                        SuggestionAdapter(it)
                    }
                }
                is Resource.Error -> {
                    Snackbar.make(binding.recyclerSuggestions, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                        .setTextColor(resources.getColor(R.color.white, theme))
                        .setAction(resources.getString(R.string.refresh)) {
                            getListSuggestion(token, agendaId)
                        }
                        .show()
                }
            }
        }
    }
}