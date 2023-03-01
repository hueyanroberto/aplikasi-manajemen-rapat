package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Suggestion
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting.MeetingUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DetailAgendaViewModel @Inject constructor(
    private val meetingUseCase: MeetingUseCase
): ViewModel() {
    fun getListSuggestion(token: String, agendaId: Int): LiveData<Resource<List<Suggestion>>> {
        return meetingUseCase.getListSuggestion(token, agendaId).asLiveData()
    }

    fun addSuggestion(token: String, agendaId: Int, suggestion: String): Flow<Resource<Suggestion>> {
        return meetingUseCase.addSuggestion(token, agendaId, suggestion)
    }
}