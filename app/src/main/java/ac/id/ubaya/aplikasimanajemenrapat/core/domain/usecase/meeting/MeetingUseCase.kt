package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import kotlinx.coroutines.flow.Flow

interface MeetingUseCase {
    fun getListOrganization(token: String, organizationId: Int): Flow<Resource<List<Meeting>>>
}