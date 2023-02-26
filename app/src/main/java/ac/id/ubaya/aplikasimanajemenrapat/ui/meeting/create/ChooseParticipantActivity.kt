package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.create

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityChooseParticipantBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseParticipantActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PARTICIPANT = "extra_participant"
        const val PARTICIPANT_RESULT_CODE = 45
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_ORGANIZATION = "extra_organization"
    }

    private lateinit var binding: ActivityChooseParticipantBinding
    private val viewModel: ChooseParticipantViewModel by viewModels()

    private var token: String = ""
    private var organizationId: Int = -1
    private val listParticipant = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra(EXTRA_TOKEN).toString()
        organizationId = intent.getIntExtra(EXTRA_ORGANIZATION, -1)
        intent.getIntegerArrayListExtra(EXTRA_PARTICIPANT)?.let { listParticipant.addAll(it) }

        getUserToBeChosen(token, organizationId)
        observeListUser()

        binding.imageChooseParticipantBack.setOnClickListener { finish() }

        binding.searchParticipant.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(query: CharSequence, p1: Int, p2: Int, p3: Int) {
                viewModel.searchUser(query.toString())
            }

            override fun afterTextChanged(p0: Editable?) { }

        })

        binding.imageChooseParticipantFinish.setOnClickListener {
            if (listParticipant.isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.participant_empty), Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent()
                intent.putExtra(EXTRA_PARTICIPANT, listParticipant)
                setResult(PARTICIPANT_RESULT_CODE, intent)
                finish()
            }
        }
    }

    private fun observeListUser() {
        binding.recyclerSelectParticipant.layoutManager = LinearLayoutManager(this)
        viewModel.listUser.observe(this) {
            val adapter = ChooseParticipantAdapter(it, listParticipant)
            binding.recyclerSelectParticipant.adapter = adapter

            binding.textChooseParticipantNoData.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun getUserToBeChosen(token: String, organizationId: Int) {
        viewModel.getUserToBeChosen(token, organizationId).observe(this) { userResource ->
            when (userResource) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    userResource.data?.let {
                        viewModel.updateListUser(it)
                    }
                }
                is Resource.Error -> {
                    Snackbar.make(binding.recyclerSelectParticipant, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                        .setTextColor(resources.getColor(R.color.white, theme))
                        .setAction(resources.getString(R.string.refresh)) {
                            getUserToBeChosen(token, organizationId)
                        }
                        .show()
                }
            }
        }
    }
}