package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.update

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting.MeetingUseCase
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditMeetingViewModel @Inject constructor(
    private val meetingUseCase: MeetingUseCase
): ViewModel() {
    fun editMeeting(token: String, title: String, location: String, description: String, startTime: Date, endTime: Date, meetingId: Int): Flow<Resource<Meeting>> {
        return meetingUseCase.editMeeting(token, title, startTime, endTime, location, description, meetingId)
    }

    fun deleteMeeting(token: String, meetingId: Int): Flow<Resource<Meeting>> {
        return meetingUseCase.deleteMeeting(token, meetingId)
    }
}