package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityEditAgendaBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditAgendaActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_AGENDA_ID = "extra_agenda_id"
        const val EXTRA_AGENDA_TASK = "extra_agenda_task"
        const val EXTRA_TOKEN = "extra_token"

        const val RESULT_CODE_EDITED = 50
        const val RESULT_CODE_DELETED = 55
    }

    private lateinit var binding: ActivityEditAgendaBinding
    private val viewModel: EditAgendaViewModel by viewModels()

    var token = ""
    var agendaId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAgendaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra(EXTRA_TOKEN).toString()
        agendaId = intent.getIntExtra(EXTRA_AGENDA_ID, -1)
        val task = intent.getStringExtra(EXTRA_AGENDA_TASK).toString()

        binding.editEditAgenda.setText(task)

        binding.buttonEditAgenda.setOnClickListener(this)
        binding.buttonDeleteAgenda.setOnClickListener(this)
        binding.imageAgendaDetailBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.imageAgendaDetailBack.id -> finish()
            binding.buttonEditAgenda.id -> {
                binding.textInputEditAgenda.error = null
                val task = binding.editEditAgenda.text.toString().trim()
                if (task.isEmpty()) {
                    binding.textInputEditAgenda.error = getString(R.string.required_field)
                } else {
                    editAgenda(task)
                }
            }
            binding.buttonDeleteAgenda.id -> {
                AlertDialog.Builder(this)
                    .setMessage(getString(R.string.agenda_delete_question))
                    .setPositiveButton(getString(R.string.delete)) {_, _ ->
                        deleteAgenda()
                    }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show()
            }
        }
    }

    private fun editAgenda(task: String) {
        lifecycleScope.launch {
            viewModel.editAgenda(token, agendaId, task).collect { agendaResource ->
                when (agendaResource) {
                    is Resource.Loading -> enableButton(false)
                    is Resource.Success -> {
                        enableButton(true)
                        Toast.makeText(this@EditAgendaActivity, getString(R.string.agenda_edited), Toast.LENGTH_SHORT).show()
                        val intent = Intent()
                        val agenda = agendaResource.data
                        intent.putExtra(EXTRA_AGENDA_TASK, agenda?.task)
                        setResult(RESULT_CODE_EDITED, intent)
                        finish()
                    }
                    is Resource.Error -> {
                        enableButton(true)
                        Snackbar.make(binding.buttonDeleteAgenda, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .show()
                    }
                }
            }
        }
    }

    private fun deleteAgenda() {
        lifecycleScope.launch {
            viewModel.deleteAgenda(token, agendaId).collect { agendaResource ->
                when (agendaResource) {
                    is Resource.Loading -> enableButton(false)
                    is Resource.Success -> {
                        enableButton(true)
                        Toast.makeText(this@EditAgendaActivity, getString(R.string.agenda_deleted), Toast.LENGTH_SHORT).show()
                        setResult(RESULT_CODE_DELETED)
                        finish()
                    }
                    is Resource.Error -> {
                        enableButton(true)
                        Snackbar.make(binding.buttonDeleteAgenda, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .show()
                    }
                }
            }
        }
    }

    private fun enableButton(isEnabled: Boolean) {
        if (isEnabled) {
            binding.progressBarEditAgenda.visibility = View.GONE
            binding.buttonEditAgenda.setBackgroundResource(R.drawable.button_primary)
            binding.buttonDeleteAgenda.setBackgroundResource(R.drawable.button_secondary_dark)
            binding.buttonEditAgenda.setOnClickListener(this@EditAgendaActivity)
            binding.buttonDeleteAgenda.setOnClickListener(this@EditAgendaActivity)
        } else {
            binding.progressBarEditAgenda.visibility = View.VISIBLE
            binding.buttonEditAgenda.setBackgroundResource(R.drawable.button_disable)
            binding.buttonDeleteAgenda.setBackgroundResource(R.drawable.button_disable)
            binding.buttonEditAgenda.setOnClickListener(null)
            binding.buttonDeleteAgenda.setOnClickListener(null)
        }
    }
}