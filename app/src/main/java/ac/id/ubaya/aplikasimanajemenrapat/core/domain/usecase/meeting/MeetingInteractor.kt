package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.repository.MeetingRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.*
import javax.inject.Inject

class MeetingInteractor @Inject constructor(
    private val meetingRepository: MeetingRepository
): MeetingUseCase {
    override fun getListOrganization(
        token: String,
        organizationId: Int
    ): Flow<Resource<List<Meeting>>> {
        return meetingRepository.getListOrganization(token, organizationId)
    }

    override fun getUserToBeChosen(token: String, organizationId: Int): Flow<Resource<List<User>>> =
        meetingRepository.getUserToBeChosen(token, organizationId)

    override fun createMeeting(
        token: String, title: String, startTime: Date, endTime: Date,
        location: String, description: String, organizationId: Int,
        participant: List<Int>, agenda: List<String>
    ): Flow<Resource<List<Meeting>>> {
        return meetingRepository.createMeeting(token, title, startTime, endTime, location, description, organizationId, participant, agenda)
    }

    override fun getMeetingDetail(token: String, meetingId: Int): Flow<Resource<Meeting?>> {
        return meetingRepository.getMeetingDetail(token, meetingId)
    }

    override fun addAgenda(
        token: String,
        meetingId: Int,
        agendas: ArrayList<String>
    ): Flow<Resource<List<Agenda>>> {
        return meetingRepository.addAgenda(token, meetingId, agendas)
    }

    override fun getListSuggestion(token: String, agendaId: Int): Flow<Resource<List<Suggestion>>> {
        return meetingRepository.getListSuggestion(token, agendaId)
    }

    override fun addSuggestion(
        token: String,
        agendaId: Int,
        suggestion: String
    ): Flow<Resource<Suggestion>> {
        return meetingRepository.addSuggestion(token, agendaId, suggestion)
    }

    override fun acceptSuggestion(token: String, suggestionId: Int): Flow<Resource<Suggestion>> {
        return meetingRepository.acceptSuggestion(token, suggestionId)
    }

    override fun deleteSuggestion(token: String, suggestionId: Int): Flow<Resource<Suggestion>> {
        return meetingRepository.deleteSuggestion(token, suggestionId)
    }

    override fun startMeeting(token: String, meetingId: Int, date: Date): Flow<Resource<Meeting>> {
        return meetingRepository.startMeeting(token, meetingId, date)
    }

    override fun joinMeeting(
        token: String,
        meetingId: Int,
        meetingCode: String,
        date: Date
    ): Flow<Resource<Meeting>> {
        return meetingRepository.joinMeeting(token, meetingId, meetingCode, date)
    }

    override fun endMeeting(token: String, meetingId: Int, date: Date, meetingNote: String): Flow<Resource<Meeting>> {
        return meetingRepository.endMeeting(token, meetingId, date, meetingNote)
    }

    override fun getMinutes(token: String, meetingId: Int): Flow<Resource<List<Agenda>>> {
        return meetingRepository.getMinutes(token, meetingId)
    }

    override fun getMeetingPointLog(token: String, meetingId: Int): Flow<Resource<List<MeetingPoint>>> {
        return meetingRepository.getMeetingPointLog(token, meetingId)
    }

    override fun editAgenda(token: String, agendaId: Int, task: String): Flow<Resource<Agenda>> {
        return meetingRepository.editAgenda(token, agendaId, task)
    }

    override fun deleteAgenda(token: String, agendaId: Int): Flow<Resource<Agenda>> {
        return meetingRepository.deleteAgenda(token, agendaId)
    }

    override fun updateAgendaStatus(token: String, agendaId: Int): Flow<Resource<Agenda>> {
        return meetingRepository.updateAgendaStatus(token, agendaId)
    }

    override fun getAgendaDetail(token: String, agendaId: Int): Flow<Resource<Agenda>> {
        return meetingRepository.getAgendaDetail(token, agendaId)
    }

    override fun editMeeting(
        token: String, title: String, startTime: Date, endTime: Date,
        location: String, description: String, meetingId: Int
    ): Flow<Resource<Meeting>> {
        return meetingRepository.editMeeting(token, title, startTime, endTime, location, description, meetingId)
    }

    override fun deleteMeeting(token: String, meetingId: Int): Flow<Resource<Meeting>> {
        return meetingRepository.deleteMeeting(token, meetingId)
    }

    override fun uploadFile(
        token: String,
        files: List<MultipartBody.Part>,
        meetingId: RequestBody
    ): Flow<Resource<List<Attachment>>> {
        return meetingRepository.uploadFile(token, files, meetingId)
    }

    override fun getListTask(token: String, meetingId: Int): Flow<Resource<List<Task>>> {
        return meetingRepository.getListTask(token, meetingId)
    }

    override fun addTask(
        token: String,
        meetingId: Int,
        userId: Int,
        title: String,
        description: String,
        deadline: Date
    ): Flow<Resource<Task>> {
        return meetingRepository.addTask(token, meetingId, userId, title, description, deadline)
    }

    override fun updateTaskStatus(token: String, taskId: Int, date: Date): Flow<Resource<Task>> {
        return meetingRepository.updateTaskStatus(token, taskId, date)
    }
}