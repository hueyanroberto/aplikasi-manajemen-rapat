package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.create

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityCreateMeetingBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.UserViewModel
import ac.id.ubaya.aplikasimanajemenrapat.ui.login.LoginActivity
import ac.id.ubaya.aplikasimanajemenrapat.ui.organization.meetingList.MeetingsListFragment
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertDateFormatWithoutTime
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertTimeFormat
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateMeetingActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_ORGANIZATION_ID = "extra_org_id"
    }

    private lateinit var binding: ActivityCreateMeetingBinding
    private val userViewModel: UserViewModel by viewModels()
    private val createMeetingViewModel: CreateMeetingViewModel by viewModels()

    private var year: Int = -1
    private var month: Int = -1
    private var day: Int = -1
    private var startHour: Int = -1
    private var startMinute: Int = -1
    private var endHour: Int = -1
    private var endMinute: Int = -1

    private val listAgenda = arrayListOf<String>()
    private val listParticipant = arrayListOf<Int>()
    private var organizationId = -1
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCreateMeeting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        organizationId = intent.getIntExtra(EXTRA_ORGANIZATION_ID, -1)

        init()

        binding.textAddAgendaCount.text = resources.getString(R.string.agenda_count, "0")
        binding.textAddParticipantCount.text = resources.getString(R.string.participant_count, "0")
    }

    private fun init() {
        userViewModel.getUser().observe(this) { user ->
            if (user.id != -1) {
                this.user = user
                userViewModel.changeGetUserStatus()

                binding.btnStartTime.setOnClickListener(this)
                binding.btnEndTime.setOnClickListener(this)
                binding.editMeetingDate.setOnClickListener(this)
                binding.constraintAddParticipant.setOnClickListener(this)
                binding.constraintAddAgenda.setOnClickListener(this)
                binding.imageCreateMeetingCreate.setOnClickListener(this)
            } else {
                Toast.makeText(this, resources.getString(R.string.user_not_login_error), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
        }
    }

    private fun createMeeting(
        token: String, title: String, startTime: Date, endTime: Date,
        location: String, description: String, organizationId: Int,
        participant: List<Int>, agenda: List<String>
    ) {
        createMeetingViewModel.createMeeting(token, title, startTime, endTime, location, description, organizationId, participant, agenda)
            .observe(this) { meetingResource ->
                when (meetingResource) {
                    is Resource.Loading -> {
                        binding.imageCreateMeetingCreate.setOnClickListener(null)
                    }
                    is Resource.Success -> {
                        MeetingsListFragment.newMeetingAdded = true
                        finish()
                    }
                    is Resource.Error -> {
                        binding.imageCreateMeetingCreate.setOnClickListener(this)
                        Snackbar.make(binding.imageCreateMeetingCreate, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .show()
                    }
                }
            }
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.editMeetingDate.id -> {
                val calendar = Calendar.getInstance()
                val year = if (this.year == -1) calendar.get(Calendar.YEAR) else this.year
                val month = if (this.month == -1) calendar.get(Calendar.MONTH) else this.month
                val day = if (this.day == -1) calendar.get(Calendar.DAY_OF_MONTH) else this.day

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
                val calenderTime = Calendar.getInstance()
                val hour = if (this.startHour == -1) calenderTime.get(Calendar.HOUR_OF_DAY) else this.startHour
                val minute = if (this.startMinute == -1) calenderTime.get(Calendar.MINUTE) else this.startMinute

                val timePicker = TimePickerDialog(this, { _, h, m ->
                    val calendarGet = Calendar.getInstance()
                    calendarGet.set(0, 0, 0, h, m)
                    binding.textStartTime.text = convertTimeFormat(calendarGet.time)
                    binding.textStartTime.setTextColor(resources.getColor(R.color.black, theme))
                    this.startHour = h
                    this.startMinute = m
                }, hour, minute, true)

                timePicker.show()
            }
            binding.btnEndTime.id -> {
                val calenderTime = Calendar.getInstance()
                val hour = if (this.endHour == -1) calenderTime.get(Calendar.HOUR_OF_DAY) else this.endHour
                val minute = if (this.endMinute == -1) calenderTime.get(Calendar.MINUTE) else this.endMinute

                val timePicker = TimePickerDialog(this, { _, h, m ->
                    val calendarGet = Calendar.getInstance()
                    calendarGet.set(0, 0, 0, h, m)
                    binding.textEndTime.text = convertTimeFormat(calendarGet.time)
                    binding.textEndTime.setTextColor(resources.getColor(R.color.black, theme))
                    this.endHour = h
                    this.endMinute = m
                }, hour, minute, true)

                timePicker.show()
            }
            binding.constraintAddAgenda.id -> {
                val intent = Intent(this, AddAgendaActivity::class.java)
                intent.putExtra(AddAgendaActivity.EXTRA_AGENDA, listAgenda)
                resultLauncher.launch(intent)
            }
            binding.constraintAddParticipant.id -> {
                val intent = Intent(this, ChooseParticipantActivity::class.java)
                intent.putExtra(ChooseParticipantActivity.EXTRA_ORGANIZATION, organizationId)
                intent.putExtra(ChooseParticipantActivity.EXTRA_TOKEN, user.token)
                intent.putExtra(ChooseParticipantActivity.EXTRA_PARTICIPANT, listParticipant)
                resultLauncher.launch(intent)
            }
            binding.imageCreateMeetingCreate.id -> {
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
                            createMeeting(
                                token = user.token.toString(),
                                title = title,
                                location = location,
                                description = description,
                                startTime = startTime.time,
                                endTime = endTime.time,
                                participant = listParticipant,
                                agenda = listAgenda,
                                organizationId = organizationId
                            )
                        }
                    }
                }
            }
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.data != null) {
            when (result.resultCode) {
                AddAgendaActivity.AGENDA_RESULT_CODE -> {
                    result.data?.getStringArrayListExtra(AddAgendaActivity.EXTRA_AGENDA)?.let {
                        listAgenda.clear()
                        listAgenda.addAll(it)
                        binding.textAddAgendaCount.text = resources.getString(R.string.agenda_count, listAgenda.size.toString())
                    }
                }
                ChooseParticipantActivity.PARTICIPANT_RESULT_CODE -> {
                    result.data?.getIntegerArrayListExtra(ChooseParticipantActivity.EXTRA_PARTICIPANT)?.let {
                        listParticipant.clear()
                        listParticipant.addAll(it)
                        binding.textAddParticipantCount.text = resources.getString(R.string.participant_count, listParticipant.size.toString())
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