package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting.MeetingUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MeetingViewModel @Inject constructor(
    private val meetingUseCase: MeetingUseCase
): ViewModel() {
    fun getMeetingDetail(token: String, meetingId: Int): LiveData<Resource<Meeting?>> {
        return meetingUseCase.getMeetingDetail(token, meetingId).asLiveData()
    }

    fun startMeeting(token: String, meetingId: Int, date: Date): Flow<Resource<Meeting>> {
        return meetingUseCase.startMeeting(token, meetingId, date)
    }

    fun joinMeeting(token: String, meetingId: Int, meetingCode: String, date: Date): Flow<Resource<Meeting>> {
        return meetingUseCase.joinMeeting(token, meetingId, meetingCode, date)
    }

    fun endMeeting(token: String, meetingId: Int, date: Date, meetingNote: String): Flow<Resource<Meeting>> {
        return meetingUseCase.endMeeting(token, meetingId, date, meetingNote)
    }
}