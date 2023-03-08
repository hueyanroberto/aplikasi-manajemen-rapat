package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Agenda
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting.MeetingUseCase
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class EditAgendaViewModel @Inject constructor(
    private val meetingUseCase: MeetingUseCase
): ViewModel() {

    fun editAgenda(token: String, agendaId: Int, task: String): Flow<Resource<Agenda>> {
        return meetingUseCase.editAgenda(token, agendaId, task)
    }

    fun deleteAgenda(token: String, agendaId: Int): Flow<Resource<Agenda>> {
        return meetingUseCase.deleteAgenda(token, agendaId)
    }
}