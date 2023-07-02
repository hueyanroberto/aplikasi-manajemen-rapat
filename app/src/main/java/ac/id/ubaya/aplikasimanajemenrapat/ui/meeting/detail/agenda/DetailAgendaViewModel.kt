package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Agenda
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

    fun acceptSuggestion(token: String, suggestionId: Int): Flow<Resource<Suggestion>> {
        return meetingUseCase.acceptSuggestion(token, suggestionId)
    }

    fun deleteSuggestion(token: String, suggestionId: Int): Flow<Resource<Suggestion>> {
        return meetingUseCase.deleteSuggestion(token, suggestionId)
    }

    fun updateAgendaStatus(token: String, agendaId: Int): Flow<Resource<Agenda>> {
        return meetingUseCase.updateAgendaStatus(token, agendaId)
    }

    fun getAgendaDetail(token: String, agendaId: Int): Flow<Resource<Agenda>> {
        return meetingUseCase.getAgendaDetail(token, agendaId)
    }
}