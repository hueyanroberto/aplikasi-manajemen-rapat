package ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Agenda
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.collections.ArrayList

interface IMeetingRepository {
    fun getListOrganization(token: String, organizationId: Int): Flow<Resource<List<Meeting>>>
    fun getUserToBeChosen(token: String, organizationId: Int): Flow<Resource<List<User>>>
    fun createMeeting(
        token: String, title: String, startTime: Date, endTime: Date,
        location: String, description: String, organizationId: Int,
        participant: List<Int>, agenda: List<String>
    ): Flow<Resource<List<Meeting>>>
    fun getMeetingDetail(token: String, meetingId: Int): Flow<Resource<Meeting?>>
    fun addAgenda(token: String, meetingId: Int, agendas: ArrayList<String>): Flow<Resource<List<Agenda>>>
}