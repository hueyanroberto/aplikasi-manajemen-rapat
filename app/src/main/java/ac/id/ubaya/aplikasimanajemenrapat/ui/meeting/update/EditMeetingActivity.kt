package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.update

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityEditMeetingBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertDateFormatWithoutTime
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertTimeFormat
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class EditMeetingActivity: AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_MEETING = "extra_meeting"
        const val EXTRA_TOKEN = "extra_token"

        const val RESULT_CODE_DELETE = 90
    }

    private lateinit var binding: ActivityEditMeetingBinding
    private val viewModel: EditMeetingViewModel by viewModels()

    private var meeting: Meeting? = null
    private var token: String = ""

    private var year: Int = -1
    private var month: Int = -1
    private var day: Int = -1
    private var startHour: Int = -1
    private var startMinute: Int = -1
    private var endHour: Int = -1
    private var endMinute: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCreateMeeting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        token = intent.getStringExtra(EXTRA_TOKEN).toString()
        meeting = if (Build.VERSION.SDK_INT > 33) {
            intent.getParcelableExtra(EXTRA_MEETING, Meeting::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_MEETING)
        }

        if (meeting == null) {
            finish()
            Toast.makeText(this, getString(R.string.internal_error_message), Toast.LENGTH_SHORT).show()
        } else {
            init(meeting)
        }

    }

    private fun init(meeting: Meeting?) {
        binding.editMeetingTitle.setText(meeting?.title)
        binding.editMeetingDescription.setText(meeting?.description)
        binding.editMeetingLocation.setText(meeting?.location)
        meeting?.startTime?.let {
            binding.editMeetingDate.setText(convertDateFormatWithoutTime(it))
            binding.textStartTime.text = convertTimeFormat(it)

            val calendar = Calendar.getInstance()
            calendar.time = it
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
            startHour = calendar.get(Calendar.HOUR_OF_DAY)
            startMinute = calendar.get(Calendar.MINUTE)
        }
        meeting?.endTime?.let {
            binding.textEndTime.text = convertTimeFormat(it)

            val calendar = Calendar.getInstance()
            calendar.time = it
            endHour = calendar.get(Calendar.HOUR_OF_DAY)
            endMinute = calendar.get(Calendar.MINUTE)
        }

        binding.btnStartTime.setOnClickListener(this)
        binding.btnEndTime.setOnClickListener(this)
        binding.editMeetingDate.setOnClickListener(this)
        binding.buttonEditMeeting.setOnClickListener(this)
        binding.buttonDeleteMeeting.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.editMeetingDate.id -> {
                val datePicker = DatePickerDialog(this, { _, y, m, d ->
                    val calendarGet = Calendar.getInstance()
                    calendarGet.set(y, m, d)
                    val date = calendarGet.time

                    val dateString = convertDateFormatWithoutTime(date)
                    binding.editMeetingDate.setText(dateString)

                    this.year = y
                    this.month = m
                    this.day = d
                }, year, month, day)

                datePicker.show()
            }
            binding.btnStartTime.id -> {

                val timePicker = TimePickerDialog(this, { _, h, m ->
                    val calendarGet = Calendar.getInstance()
                    calendarGet.set(0, 0, 0, h, m)

                    binding.textStartTime.text = convertTimeFormat(calendarGet.time)
                    binding.textStartTime.setTextColor(resources.getColor(R.color.black, theme))
                    this.startHour = h
                    this.startMinute = m

                }, startHour, startMinute, true)

                timePicker.show()
            }
            binding.btnEndTime.id -> {

                val timePicker = TimePickerDialog(this, { _, h, m ->
                    val calendarGet = Calendar.getInstance()
                    calendarGet.set(0, 0, 0, h, m)

                    binding.textEndTime.text = convertTimeFormat(calendarGet.time)
                    binding.textEndTime.setTextColor(resources.getColor(R.color.black, theme))
                    this.endHour = h
                    this.endMinute = m

                }, endHour, endMinute, true)

                timePicker.show()
            }
            binding.buttonEditMeeting.id -> {
                binding.textInputMeetingTitle.error = null
                binding.textInputMeetingLocation.error = null
                binding.textInputMeetingDescription.error = null
                binding.textInputMeetingDate.error = null

                val title = binding.editMeetingTitle.text.toString().trim()
                val location = binding.editMeetingLocation.text.toString().trim()
                val description = binding.editMeetingDescription.text.toString().trim()

                when {
                    title.isEmpty() -> {
                        binding.textInputMeetingTitle.error = resources.getString(R.string.required_field)
                    }
                    location.isEmpty() -> {
                        binding.textInputMeetingLocation.error = resources.getString(R.string.required_field)
                    }
                    description.isEmpty() -> {
                        binding.textInputMeetingDescription.error = resources.getString(R.string.required_field)
                    }
                    year == -1 || month == -1 || day == -1 -> {
                        binding.textInputMeetingDate.error = resources.getString(R.string.required_field)
                    }
                    startHour == -1 || startMinute == -1 -> {
                        Toast.makeText(this, "Something", Toast.LENGTH_SHORT).show()
                    }
                    endHour == -1 || endMinute == -1 -> {
                        Toast.makeText(this, "Something", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        val startTime = Calendar.getInstance()
                        startTime.set(year, month, day, startHour, startMinute)
                        val endTime = Calendar.getInstance()
                        endTime.set(year, month, day, endHour, endMinute)

                        if (endTime.time <= startTime.time) {
                            binding.textEndTime.setTextColor(resources.getColor(R.color.secondary_dark, theme))
                            Toast.makeText(this, "Something", Toast.LENGTH_SHORT).show()
                        } else {
                            editMeeting(
                                token = token, title = title, location = location, description = description,
                                startTime = startTime.time, endTime = endTime.time, meetingId = meeting!!.id
                            )
                        }
                    }
                }
            }
            binding.buttonDeleteMeeting.id -> {
                AlertDialog.Builder(this)
                    .setMessage(getString(R.string.agenda_meeting_question))
                    .setPositiveButton(getString(R.string.delete)) {_, _ ->
                        deleteMeeting(token, meeting!!.id)
                    }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show()
            }
        }
    }

    private fun editMeeting(token: String, title: String, location: String, description: String, startTime: Date, endTime: Date, meetingId: Int) {
        lifecycleScope.launch {
            viewModel.editMeeting(token, title, location, description, startTime, endTime, meetingId).collect {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Toast.makeText(this@EditMeetingActivity, getString(R.string.meeting_edited), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is Resource.Error -> {
                        Snackbar.make(binding.buttonDeleteMeeting, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .show()
                    }
                }
            }
        }
    }

    private fun deleteMeeting(token: String, meetingId: Int) {
        lifecycleScope.launch {
            viewModel.deleteMeeting(token, meetingId).collect {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Toast.makeText(this@EditMeetingActivity, getString(R.string.meeting_deleted), Toast.LENGTH_SHORT).show()
                        setResult(RESULT_CODE_DELETE)
                        finish()
                    }
                    is Resource.Error -> {
                        Snackbar.make(binding.buttonDeleteMeeting, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .show()
                    }
                }
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