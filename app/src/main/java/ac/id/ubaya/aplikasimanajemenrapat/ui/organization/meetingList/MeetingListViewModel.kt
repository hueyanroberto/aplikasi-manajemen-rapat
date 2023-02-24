package ac.id.ubaya.aplikasimanajemenrapat.ui.organization.meetingList

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting.MeetingUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MeetingListViewModel @Inject constructor(
    private val meetingUseCase: MeetingUseCase,
): ViewModel() {
    fun getListMeeting(token: String, organizationId: Int): LiveData<Resource<List<Meeting>>> {
        return meetingUseCase.getListOrganization(token, organizationId).asLiveData()
    }
}