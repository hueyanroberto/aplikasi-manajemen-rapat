package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Agenda
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Suggestion
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityDetailAgendaBinding
import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailAgendaActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_AGENDA = "extra_agenda"
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_ROLE = "extra_role"
    }

    private lateinit var binding: ActivityDetailAgendaBinding
    private val viewModel: DetailAgendaViewModel by viewModels()

    private var agenda: Agenda? = null
    private var token: String = ""
    private var role: Int = -1

    private lateinit var adapter: SuggestionAdapter
    private val listSuggestion = arrayListOf<Suggestion>()

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
        role = intent.getIntExtra(EXTRA_ROLE, -1)

        binding.textAgendaTitle.text = agenda?.task.toString()
        binding.recyclerSuggestions.layoutManager = LinearLayoutManager(this)
        adapter = SuggestionAdapter(listSuggestion, role)

        if (role == 1) {
            adapter.setOnItemClickedCallback(object: SuggestionAdapter.OnItemClickedCallback {
                override fun onItemClickedCallback(data: Suggestion, position: Int) {
                    val message = if (data.accepted == 0) {
                        resources.getString(R.string.accept_suggestion_question)
                    } else {
                        resources.getString(R.string.cancel_accept_suggestion_question)
                    }

                    AlertDialog.Builder(this@DetailAgendaActivity)
                        .setMessage(message)
                        .setPositiveButton(resources.getString(R.string.ok)) {_, _ ->
                            buttonAcceptClicked(data, position)
                        }
                        .setNegativeButton(resources.getString(R.string.cancel), null)
                        .show()
                }
            })
        }

        binding.recyclerSuggestions.adapter = adapter

        agenda?.id?.let {
            getListSuggestion(token, it)
            binding.imageAgendaDetailBack.setOnClickListener(this)

            binding.imageSuggestionSend.setOnClickListener {
                Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getListSuggestion(token: String, agendaId: Int) {
        viewModel.getListSuggestion(token, agendaId).observe(this) { suggestionResource ->
            when (suggestionResource) {
                is Resource.Loading -> {
                    binding.progressBarAgendaDetail.visibility = View.VISIBLE
                    binding.imageSuggestionSend.setOnClickListener {
                        Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Success -> {
                    binding.progressBarAgendaDetail.visibility = View.GONE
                    binding.imageSuggestionSend.setOnClickListener(this)
                    listSuggestion.clear()
                    suggestionResource.data?.let {
                        listSuggestion.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> {
                    binding.progressBarAgendaDetail.visibility = View.GONE
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

    private fun addSuggestion(token: String, agendaId: Int, suggestion: String) {
        lifecycleScope.launch {
            viewModel.addSuggestion(token, agendaId, suggestion).collect { suggestionResource ->
                when (suggestionResource) {
                    is Resource.Loading -> {
                        binding.progressBarAgendaDetail.visibility = View.VISIBLE
                        binding.imageSuggestionSend.setOnClickListener {
                            Toast.makeText(this@DetailAgendaActivity, "Please wait", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is Resource.Success -> {
                        binding.progressBarAgendaDetail.visibility = View.GONE
                        binding.imageSuggestionSend.setOnClickListener(this@DetailAgendaActivity)
                        binding.editSuggestion.setText("")
                        val newSuggestion = suggestionResource.data
                        newSuggestion?.let {
                            listSuggestion.add(it)
                            adapter.notifyItemInserted(listSuggestion.size - 1)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBarAgendaDetail.visibility = View.GONE
                        binding.imageSuggestionSend.setOnClickListener(this@DetailAgendaActivity)
                        Snackbar.make(binding.recyclerSuggestions, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .show()
                    }
                }
            }
        }
    }

    private fun buttonAcceptClicked(data: Suggestion, position: Int) {
        lifecycleScope.launch {
            viewModel.acceptSuggestion(token, data.id).collect { suggestionResource ->
                when (suggestionResource) {
                    is Resource.Loading -> {
                        binding.progressBarAgendaDetail.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarAgendaDetail.visibility = View.GONE
                        val newSuggestion = suggestionResource.data
                        newSuggestion?.let {
                            data.accepted = it.accepted
                            adapter.notifyItemChanged(position)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBarAgendaDetail.visibility = View.GONE
                        Snackbar.make(binding.recyclerSuggestions, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .show()
                    }
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.imageAgendaDetailBack.id -> finish()
            binding.imageSuggestionSend.id -> {
                val suggestion = binding.editSuggestion.text.toString().trim()
                if (suggestion.isNotEmpty()) {
                    addSuggestion(token, agenda?.id!!, suggestion)
                }
            }
        }
    }
}