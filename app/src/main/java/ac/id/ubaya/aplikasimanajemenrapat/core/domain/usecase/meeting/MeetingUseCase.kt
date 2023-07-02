package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    fun deleteSuggestion(token: String, suggestionId: Int): Flow<Resource<Suggestion>>
    fun startMeeting(token: String, meetingId: Int, date: Date): Flow<Resource<Meeting>>
    fun joinMeeting(token: String, meetingId: Int, meetingCode: String, date: Date): Flow<Resource<Meeting>>
    fun endMeeting(token: String, meetingId: Int, date: Date, meetingNote: String): Flow<Resource<Meeting>>
    fun getMinutes(token: String, meetingId: Int): Flow<Resource<List<Agenda>>>
    fun getMeetingPointLog(token: String, meetingId: Int): Flow<Resource<List<MeetingPoint>>>
    fun editAgenda(token: String, agendaId: Int, task: String): Flow<Resource<Agenda>>
    fun deleteAgenda(token: String, agendaId: Int): Flow<Resource<Agenda>>
    fun updateAgendaStatus(token: String, agendaId: Int): Flow<Resource<Agenda>>
    fun getAgendaDetail(token: String, agendaId: Int): Flow<Resource<Agenda>>
    fun editMeeting(token: String, title: String, startTime: Date, endTime: Date, location: String, description: String, meetingId: Int): Flow<Resource<Meeting>>
    fun deleteMeeting(token: String, meetingId: Int): Flow<Resource<Meeting>>
    fun uploadFile(token: String, files: List<MultipartBody.Part>, meetingId: RequestBody):Flow<Resource<List<Attachment>>>
    fun getListTask(token: String, meetingId: Int): Flow<Resource<List<Task>>>
    fun addTask(token: String, meetingId: Int, userId: Int, title: String, description: String, deadline: Date): Flow<Resource<Task>>
    fun updateTaskStatus(token: String, taskId: Int, date: Date): Flow<Resource<Task>>
}