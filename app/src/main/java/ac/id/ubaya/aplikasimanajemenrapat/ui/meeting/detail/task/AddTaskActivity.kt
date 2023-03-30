package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.task

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Participant
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityAddTaskBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertDateFormat
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AddTaskActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_LIST_PARTICIPANT = "extra_list_participant"
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_MEETING_ID = "extra_meeting_id"
    }

    private lateinit var binding: ActivityAddTaskBinding
    private val viewModel: TaskViewModel by viewModels()

    private var year: Int = -1
    private var month: Int = -1
    private var date: Int = -1
    private var hour: Int = -1
    private var minute: Int = -1
    private var selectedUserId: Int = -1
    private var token = ""
    private var meetingId = -1

    private var arrParticipant: Array<Participant>? = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        date = calendar.get(Calendar.DAY_OF_MONTH)
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)
        token = intent.getStringExtra(EXTRA_TOKEN).toString()
        meetingId = intent.getIntExtra(EXTRA_MEETING_ID, -1)

        arrParticipant = if (Build.VERSION.SDK_INT > 33) {
            intent.getParcelableArrayExtra(EXTRA_LIST_PARTICIPANT, Participant::class.java) as Array<Participant>
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayExtra(EXTRA_LIST_PARTICIPANT)?.map {
                it as Participant
            }?.toTypedArray()
        }

        binding.editDeadline.setOnClickListener(this)
        binding.constraintAddParticipant.setOnClickListener(this)
        binding.buttonAddTask.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.editDeadline.id -> {
                val datePicker = DatePickerDialog(this, { _, y, M, d ->

                    val timePicker = TimePickerDialog(this, { _, h, m ->
                        val calendarGet = Calendar.getInstance()
                        calendarGet.set(y, M, d, h, m)
                        val date = calendarGet.time

                        binding.editDeadline.setText(convertDateFormat(date))

                        this.year = y
                        this.month = M
                        this.date = d
                        this.hour = h
                        this.minute = m

                    }, hour, minute, true)

                    timePicker.show()
                }, year, month, date)

                datePicker.show()
            }
            binding.constraintAddParticipant.id -> {
                val intent = Intent(this, AssignTaskActivity::class.java)
                intent.putExtra(AssignTaskActivity.EXTRA_PARTICIPANT, arrParticipant)
                resultLauncher.launch(intent)
            }
            binding.buttonAddTask.id -> {
                binding.textInputTaskTitle.error = null
                binding.textInputTaskDescription.error = null
                binding.textInputDeadline.error = null

                val title = binding.editTaskTitle.text.toString().trim()
                val desc = binding.editTaskDescription.text.toString().trim()
                val dateString = binding.editDeadline.text.toString().trim()

                if (title.isEmpty()) {
                    binding.textInputTaskTitle.error = resources.getString(R.string.required_field)
                } else if (desc.isEmpty()) {
                    binding.textInputTaskTitle.error = resources.getString(R.string.required_field)
                } else if (dateString.isEmpty()) {
                    binding.textInputDeadline.error = resources.getString(R.string.required_field)
                } else if (selectedUserId == -1) {
                    Toast.makeText(this, "Please assign a member to this task", Toast.LENGTH_SHORT).show()
                } else {
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, date, hour, minute)
                    addTask(token, meetingId, selectedUserId, title, desc, calendar.time)
                }
            }
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.data != null) {
            when (result.resultCode) {
                AssignTaskActivity.ASSIGN_RESULT_CODE -> {
                    val participant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        result.data?.getParcelableExtra(AssignTaskActivity.EXTRA_PARTICIPANT, Participant::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        result.data?.getParcelableExtra(AssignTaskActivity.EXTRA_PARTICIPANT)
                    }

                    participant?.let {
                        this.selectedUserId = it.id
                        binding.textAddTaskAssignedPerson.text = it.name
                        binding.textAddTaskAssignedPerson.setTextColor(resources.getColor(R.color.black, theme))
                    }
                }
            }
        }
    }

    private fun addTask(token: String, meetingId: Int, userId: Int, title: String, description: String, deadline: Date) {
        lifecycleScope.launch {
            viewModel.addTask(token, meetingId, userId, title, description, deadline).collect { taskResource ->
                when (taskResource) {
                    is Resource.Loading -> {
                        binding.buttonAddTask.setBackgroundResource(R.drawable.button_disable)
                        binding.buttonAddTask.setOnClickListener(null)
                    }
                    is Resource.Success -> {
                        Toast.makeText(this@AddTaskActivity, getString(R.string.task_added), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is Resource.Error -> {
                        binding.buttonAddTask.setBackgroundResource(R.drawable.button_primary)
                        binding.buttonAddTask.setOnClickListener(this@AddTaskActivity)
                        Snackbar.make(binding.buttonAddTask, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .show()
                    }
                }
            }
        }
    }
}