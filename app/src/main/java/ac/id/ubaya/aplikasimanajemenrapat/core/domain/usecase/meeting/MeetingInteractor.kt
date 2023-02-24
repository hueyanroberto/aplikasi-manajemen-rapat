package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.repository.MeetingRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import kotlinx.coroutines.flow.Flow
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
}