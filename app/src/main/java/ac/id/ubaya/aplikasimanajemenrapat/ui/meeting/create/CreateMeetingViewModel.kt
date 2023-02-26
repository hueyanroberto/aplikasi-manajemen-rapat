package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.create

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting.MeetingUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateMeetingViewModel @Inject constructor(
    private val meetingUseCase: MeetingUseCase
): ViewModel() {
    fun createMeeting(
        token: String, title: String, startTime: Date, endTime: Date,
        location: String, description: String, organizationId: Int,
        participant: List<Int>, agenda: List<String>
    ): LiveData<Resource<List<Meeting>>> {
        return meetingUseCase.createMeeting(
            token, title, startTime, endTime, location, description, organizationId, participant, agenda
        ).asLiveData()
    }
}