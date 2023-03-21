package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.create

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityAddAgendaBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAgendaActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_AGENDA = "extra_agenda"
        const val AGENDA_RESULT_CODE = 46
    }

    private lateinit var binding: ActivityAddAgendaBinding
    private val listEditText = arrayListOf<EditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAgendaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val listAgenda = intent.getStringArrayListExtra(EXTRA_AGENDA)
        if (listAgenda?.size == 0) {
            createEditText(false)
        } else {
            listAgenda?.forEach {
                createEditText(it)
            }
        }

        binding.buttonAddAgenda.setOnClickListener {
            val text = listEditText[listEditText.size - 1].text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.agenda_empty), Toast.LENGTH_SHORT).show()
            } else {
                createEditText(true)
            }
        }

        binding.imageAddAgendaFinish.setOnClickListener {
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

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_AGENDA, newListAgenda)
                setResult(AGENDA_RESULT_CODE, resultIntent)
                finish()
            }
        }
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

    private fun createEditText(text: String) {
        val relativeParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val editText = EditText(this)
        editText.layoutParams = relativeParams
        editText.inputType = InputType.TYPE_CLASS_TEXT
        editText.hint = "Agenda " + (listEditText.size + 1)
        editText.setText(text)
        binding.relativeAgendaContainer.addView(editText)
        listEditText.add(editText)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}