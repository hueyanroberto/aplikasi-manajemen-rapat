package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Agenda
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Suggestion
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityDetailAgendaBinding
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
        const val EXTRA_MEETING_STATUS = "extra_meeting_status"
    }

    private lateinit var binding: ActivityDetailAgendaBinding
    private val viewModel: DetailAgendaViewModel by viewModels()

    private var agenda: Agenda? = null
    private var token: String = ""
    private var role: Int = -1
    private var meetingStatus: Int = -1

    private lateinit var adapter: SuggestionAdapter
    private val listSuggestion = arrayListOf<Suggestion>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAgendaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.imageAddAgendaMore.visibility = View.GONE

        @Suppress("DEPRECATION")
        agenda = if (Build.VERSION.SDK_INT > 33) {
            intent.getParcelableExtra(EXTRA_AGENDA, Agenda::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_AGENDA)
        }
        token = intent.getStringExtra(EXTRA_TOKEN).toString()
        role = intent.getIntExtra(EXTRA_ROLE, -1)
        meetingStatus = intent.getIntExtra(EXTRA_MEETING_STATUS, -1)

        binding.textAgendaTitle.text = agenda?.task.toString()
        binding.textAgendaStatus.text = if (agenda?.completed == 0) {
            binding.constraintSuggestionAdd.visibility = View.VISIBLE
            resources.getString(R.string.suggestion_status, resources.getString(R.string.not_completed))
        } else {
            binding.constraintSuggestionAdd.visibility = View.GONE
            resources.getString(R.string.suggestion_status, resources.getString(R.string.completed))
        }
        binding.recyclerSuggestions.layoutManager = LinearLayoutManager(this)
        adapter = SuggestionAdapter(listSuggestion, role, meetingStatus, agenda!!.completed)

        if (role == 1 && meetingStatus != 2) {

            if (agenda?.completed == 0 && meetingStatus == 1) {
                binding.buttonAgendaDone.visibility = View.VISIBLE
                binding.buttonAgendaDone.setOnClickListener {
                    AlertDialog.Builder(this@DetailAgendaActivity)
                        .setMessage(resources.getString(R.string.mark_agenda_done_message))
                        .setPositiveButton(resources.getString(R.string.mark_as_done)) { _,_ ->
                            agenda?.let {
                                updateAgendaStatus(token, it.id)
                            }
                        }
                        .setNegativeButton(resources.getString(R.string.cancel), null)
                        .show()
                }
            } else {
                binding.buttonAgendaDone.visibility = View.GONE
                binding.buttonAgendaDone.setOnClickListener(null)
                binding.constraintSuggestionAdd.visibility = View.GONE
            }

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

                override fun onDeleteClickedCallback(data: Suggestion, position: Int) {
                    AlertDialog.Builder(this@DetailAgendaActivity)
                        .setMessage(getString(R.string.delete_suggestion_message))
                        .setPositiveButton(resources.getString(R.string.delete)) {_, _ ->
                            deleteSuggestion(data, position)
                        }
                        .setNegativeButton(resources.getString(R.string.cancel), null)
                        .show()
                }
            })

            if (meetingStatus == 0) {
                binding.imageAddAgendaMore.visibility = View.VISIBLE
                binding.buttonAgendaDone.visibility = View.GONE
                binding.imageAddAgendaMore.setOnClickListener {
                    val popupMenu = PopupMenu(this, it)
                    popupMenu.inflate(R.menu.menu_agenda)
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.item_agenda_edit -> {
                                val intent = Intent(this, EditAgendaActivity::class.java)
                                intent.putExtra(EditAgendaActivity.EXTRA_TOKEN, token)
                                intent.putExtra(EditAgendaActivity.EXTRA_AGENDA_ID, agenda?.id)
                                intent.putExtra(EditAgendaActivity.EXTRA_AGENDA_TASK, agenda?.task)
                                resultLauncher.launch(intent)
                            }
                        }
                        true
                    }
                    popupMenu.show()
                }
            }
        }

        if (meetingStatus == 2) {
            binding.constraintSuggestionAdd.visibility = View.GONE
        }

        binding.recyclerSuggestions.adapter = adapter

        agenda?.id?.let {
            getListSuggestion(token, it)
            getAgendaDetail(token, it)

            binding.swipeDetailAgenda.setOnRefreshListener {
                getListSuggestion(token, it)
                getAgendaDetail(token, it)
            }

            binding.imageSuggestionSend.setOnClickListener {
                Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAgendaStatus(token: String, agendaId: Int) {
        lifecycleScope.launch {
            viewModel.updateAgendaStatus(token, agendaId).collect { agendaResource ->
                when (agendaResource) {
                    is Resource.Loading -> {
                        binding.progressBarAgendaDetail.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarAgendaDetail.visibility = View.GONE
                        if (agendaResource.data?.completed == 1) {
                            agenda?.completed = 1
                            binding.buttonAgendaDone.setBackgroundResource(R.drawable.button_disable)
                            binding.buttonAgendaDone.setOnClickListener(null)
                            binding.constraintSuggestionAdd.visibility = View.GONE
                            binding.buttonAgendaDone.visibility = View.GONE
                            adapter.updateAgendaStatus(1)
                            getListSuggestion(token, agendaId)
                            Toast.makeText(this@DetailAgendaActivity, resources.getText(R.string.success), Toast.LENGTH_SHORT).show()
                        } else {
                            AlertDialog.Builder(this@DetailAgendaActivity)
                                .setTitle(resources.getString(R.string.cannot_mark_agenda_done))
                                .setMessage(resources.getString(R.string.no_suggestion_accepted))
                                .setPositiveButton(resources.getString(R.string.ok), null)
                                .show()
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBarAgendaDetail.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun getAgendaDetail(token: String, agendaId: Int) {
        lifecycleScope.launch {
            viewModel.getAgendaDetail(token, agendaId).collect { agendaResource ->
                when (agendaResource) {
                    is Resource.Loading -> { }
                    is Resource.Success -> {
                        agenda = agendaResource.data
                        binding.textAgendaStatus.text = if (agenda?.completed == 0) {
                            binding.constraintSuggestionAdd.visibility = View.VISIBLE
                            resources.getString(R.string.suggestion_status, resources.getString(R.string.not_completed))
                        } else {
                            binding.constraintSuggestionAdd.visibility = View.GONE
                            resources.getString(R.string.suggestion_status, resources.getString(R.string.completed))
                        }
                        binding.textAgendaTitle.text = agenda?.task
                    }
                    is Resource.Error -> {}
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getListSuggestion(token: String, agendaId: Int) {
        viewModel.getListSuggestion(token, agendaId).observe(this) { suggestionResource ->
            when (suggestionResource) {
                is Resource.Loading -> {
                    binding.swipeDetailAgenda.isRefreshing = false
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
                        binding.viewEmpty.root.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
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
                            binding.viewEmpty.root.visibility = View.GONE
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

    private fun deleteSuggestion(data: Suggestion, position: Int) {
        lifecycleScope.launch {
            viewModel.deleteSuggestion(token, data.id).collect { suggestionResource ->
                when (suggestionResource) {
                    is Resource.Loading -> {
                        binding.progressBarAgendaDetail.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarAgendaDetail.visibility = View.GONE
                        val deletedSuggestion = suggestionResource.data
                        deletedSuggestion?.let {
                            for ((i, suggestion) in listSuggestion.withIndex()) {
                                if (suggestion.id == it.id) {
                                    listSuggestion.removeAt(i)
                                    adapter.notifyItemRemoved(position)
                                    if (listSuggestion.size == 0) binding.viewEmpty.root.visibility = View.VISIBLE
                                    break
                                }
                            }
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
            binding.imageSuggestionSend.id -> {
                val suggestion = binding.editSuggestion.text.toString().trim()
                if (suggestion.isNotEmpty()) {
                    addSuggestion(token, agenda?.id!!, suggestion)
                }
            }
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        when (it.resultCode){
            EditAgendaActivity.RESULT_CODE_EDITED -> {
                val task = it.data?.getStringExtra(EditAgendaActivity.EXTRA_AGENDA_TASK).toString()
                agenda?.task = task
                binding.textAgendaTitle.text = agenda?.task
            }
            EditAgendaActivity.RESULT_CODE_DELETED -> {
                finish()
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