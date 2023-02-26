package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.repository.MeetingRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import kotlinx.coroutines.flow.Flow
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
}