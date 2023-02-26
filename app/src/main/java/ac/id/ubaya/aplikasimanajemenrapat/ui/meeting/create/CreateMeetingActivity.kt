package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.create

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityCreateMeetingBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.UserViewModel
import ac.id.ubaya.aplikasimanajemenrapat.ui.login.LoginActivity
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertDateFormatWithoutTime
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertTimeFormat
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateMeetingActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_ORGANIZATION_ID = "extra_org_id"
    }

    private lateinit var binding: ActivityCreateMeetingBinding
    private val userViewModel: UserViewModel by viewModels()
    private val createMeetingViewModel: CreateMeetingViewModel by viewModels()

    private var year: Int? = null
    private var month: Int? = null
    private var day: Int? = null
    private var startHour: Int? = null
    private var startMinute: Int? = null
    private var endHour: Int? = null
    private var endMinute: Int? = null

    private val listAgenda = arrayListOf<String>()
    private val listParticipant = arrayListOf<Int>()
    private var organizationId = -1
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    private fun createMeeting() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.editMeetingDate.id -> {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

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
            binding.btnStartTime.id, binding.btnEndTime.id -> {
                val calenderTime = Calendar.getInstance()
                val hour = calenderTime.get(Calendar.HOUR_OF_DAY)
                val minute = calenderTime.get(Calendar.MINUTE)

                val timePicker = TimePickerDialog(this, { _, h, m ->
                    val calendarGet = Calendar.getInstance()
                    calendarGet.set(0, 0, 0, h, m)
                    when (v.id) {
                        binding.btnStartTime.id -> {
                            binding.textStartTime.text = convertTimeFormat(calendarGet.time)
                            this.startHour = h
                            this.startMinute = m
                        }
                        binding.btnEndTime.id -> {
                            binding.textEndTime.text = convertTimeFormat(calendarGet.time)
                            this.endHour = h
                            this.endMinute = m
                        }
                    }
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
            binding.imageCreateMeetingCreate.id -> createMeeting()
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
}