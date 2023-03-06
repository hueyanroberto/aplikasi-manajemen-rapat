package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Agenda
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Suggestion
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.util.*

interface MeetingUseCase {
    fun getListOrganization(token: String, organizationId: Int): Flow<Resource<List<Meeting>>>
    fun getUserToBeChosen(token: String, organizationId: Int): Flow<Resource<List<User>>>
    fun createMeeting(
        token: String, title: String, startTime: Date, endTime: Date,
        location: String, description: String, organizationId: Int,
        participant: List<Int>, agenda: List<String>
    ): Flow<Resource<List<Meeting>>>
    fun getMeetingDetail(token: String, meetingId: Int): Flow<Resource<Meeting?>>
    fun addAgenda(token: String, meetingId: Int, agendas: ArrayList<String>): Flow<Resource<List<Agenda>>>
    fun getListSuggestion(token: String, agendaId: Int): Flow<Resource<List<Suggestion>>>
    fun addSuggestion(token: String, agendaId: Int, suggestion: String): Flow<Resource<Suggestion>>
    fun acceptSuggestion(token: String, suggestionId: Int): Flow<Resource<Suggestion>>
    fun startMeeting(token: String, meetingId: Int, date: Date): Flow<Resource<Meeting>>
    fun joinMeeting(token: String, meetingId: Int, meetingCode: String, date: Date): Flow<Resource<Meeting>>
    fun endMeeting(token: String, meetingId: Int, date: Date): Flow<Resource<Meeting>>
}