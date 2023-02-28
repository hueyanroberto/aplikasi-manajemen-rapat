package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityAddAgendaBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeetingAddAgendaActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_MEETING_ID = "extra_meeting_id"
        const val EXTRA_TOKEN = "extra_token"
    }

    private lateinit var binding: ActivityAddAgendaBinding
    private val listEditText = arrayListOf<EditText>()

    private val viewModel: MeetingAddAgendaViewModel by viewModels()

    private var meetingId = -1
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAgendaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        meetingId = intent.getIntExtra(EXTRA_MEETING_ID, -1)
        token = intent.getStringExtra(EXTRA_TOKEN).toString()

        createEditText(false)

        binding.buttonAddAgenda.setOnClickListener(this)
        binding.imageAddAgendaFinish.setOnClickListener(this)
    }

    private fun createEditText(isRequestFocus: Boolean) {
        val relativeParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val editText = EditText(this)
        editText.layoutParams = relativeParams
        editText.inputType = InputType.TYPE_CLASS_TEXT
        editText.hint = "Agenda " + (listEditText.size + 1)
        if (isRequestFocus) editText.requestFocus()
        binding.relativeAgendaContainer.addView(editText)
        listEditText.add(editText)
    }

    private fun addAgenda(agenda: ArrayList<String>) {
        viewModel.addAgenda(token, meetingId, agenda).observe(this) { agendaResource ->
            when (agendaResource) {
                is Resource.Loading -> {
                    binding.imageAddAgendaFinish.setOnClickListener(null)
                }
                is Resource.Success -> {
                    Toast.makeText(this, "Agenda has been added", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Resource.Error -> {
                    binding.imageAddAgendaFinish.setOnClickListener(this)
                    Snackbar.make(binding.buttonAddAgenda, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                        .setTextColor(resources.getColor(R.color.white, theme))
                        .setAction(resources.getString(R.string.refresh)) {
                            addAgenda(agenda)
                        }
                        .show()
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.buttonAddAgenda.id -> {
                val text = listEditText[listEditText.size - 1].text.toString().trim()
                if (text.isEmpty()) {
                    Toast.makeText(this, resources.getString(R.string.agenda_empty), Toast.LENGTH_SHORT).show()
                } else {
                    createEditText(true)
                }
            }
            binding.imageAddAgendaFinish.id -> {
                var canFinish = false
                if (listEditText.size == 1) {
                    val text = listEditText[0].text.toString().trim()
                    if (text.isNotEmpty()) canFinish = true
                    else {
                        Toast.makeText(this, resources.getString(R.string.agenda_empty), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    canFinish = true
                }

                if (canFinish) {
                    val newListAgenda = arrayListOf<String>()
                    listEditText.forEach {
                        val agendaText = it.text.toString().trim()
                        if (agendaText.isNotEmpty()) newListAgenda.add(agendaText)
                    }

                    addAgenda(newListAgenda)
                }
            }
        }
    }


}