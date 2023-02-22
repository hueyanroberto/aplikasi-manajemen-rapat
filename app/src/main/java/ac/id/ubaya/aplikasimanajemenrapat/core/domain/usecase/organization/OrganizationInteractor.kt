package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.organization

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.repository.OrganizationRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrganizationInteractor @Inject constructor(
    private val organizationRepository: OrganizationRepository
): OrganizationUseCase {
    override fun getListOrganization(token: String): Flow<Resource<List<Organization>>> = organizationRepository.getListOrganization(token)

    override fun getListFromDatabase(): Flow<List<Organization>> = organizationRepository.getListFromDatabase()

    override fun createOrganization(
        name: String,
        description: String,
        profilePic: String,
        token: String
    ): Flow<Resource<Organization?>> {
        return organizationRepository.createOrganization(name, description, profilePic, token)
    }

    override fun joinOrganization(
        token: String,
        organizationCode: String
    ): Flow<Resource<Organization?>> {
        return organizationRepository.joinOrganization(token, organizationCode)
    }
}