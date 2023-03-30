package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.task

import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Participant
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityChooseParticipantBinding
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

class AssignTaskActivity: AppCompatActivity() {
    companion object {
        const val EXTRA_PARTICIPANT = "extra_participant"
        const val ASSIGN_RESULT_CODE = 30
    }

    private lateinit var binding: ActivityChooseParticipantBinding
    private val viewModel: AssignTaskViewModel by viewModels()
    private var listParticipant = arrayListOf<Participant>()
    private var arrParticipant: Array<Participant>? = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT > 33) {
            arrParticipant = intent.getParcelableArrayExtra(EXTRA_PARTICIPANT, Participant::class.java) as Array<Participant>
        } else {
            @Suppress("DEPRECATION")

            arrParticipant = intent.getParcelableArrayExtra(EXTRA_PARTICIPANT)?.map {
                it as Participant
            }?.toTypedArray()
        }

        arrParticipant?.let {
            viewModel.submitList(it.toList())

            viewModel.listUser.observe(this) { listData ->
                binding.recyclerSelectParticipant.layoutManager = LinearLayoutManager(this)
                val adapter = AssignTaskAdapter(listData)
                adapter.setOnItemLongClickCallback(object : AssignTaskAdapter.OnItemClickCallback{
                    override fun onItemClickCallback(participant: Participant) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_PARTICIPANT, participant)
                        setResult(ASSIGN_RESULT_CODE, intent)
                        finish()
                    }
                })
                binding.recyclerSelectParticipant.adapter = adapter
            }


            binding.searchParticipant.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun onTextChanged(query: CharSequence, p1: Int, p2: Int, p3: Int) {
                    viewModel.searchParticipant(query.toString())
                }

                override fun afterTextChanged(p0: Editable?) { }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}