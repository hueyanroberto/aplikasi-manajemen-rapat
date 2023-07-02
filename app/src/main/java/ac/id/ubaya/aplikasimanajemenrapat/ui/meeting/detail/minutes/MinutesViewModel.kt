package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.minutes

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Agenda
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting.MeetingUseCase
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MinutesViewModel @Inject constructor(
    private val meetingUseCase: MeetingUseCase
): ViewModel() {
    fun getMinutes(token: String, meetingId: Int): Flow<Resource<List<Agenda>>> {
        return meetingUseCase.getMinutes(token, meetingId)
    }

    fun getMeetingDetail(token: String, meetingId: Int): Flow<Resource<Meeting?>> {
        return meetingUseCase.getMeetingDetail(token, meetingId)
    }
}