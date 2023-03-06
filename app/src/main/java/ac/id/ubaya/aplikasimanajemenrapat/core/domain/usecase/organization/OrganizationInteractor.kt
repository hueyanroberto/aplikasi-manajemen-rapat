package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.organization

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.repository.OrganizationRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
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

    override fun getOrganizationMembers(
        token: String,
        organizationId: Int
    ): Flow<Resource<List<User>>> {
        return organizationRepository.getOrganizationMembers(token, organizationId)
    }

    override fun updateRole(
        token: String,
        organizationId: Int,
        userId: Int,
        roleId: Int
    ): Flow<Resource<User>> {
        return organizationRepository.updateRole(token, organizationId, userId, roleId)
    }

    override fun editOrganization(
        token: String,
        organizationId: Int,
        name: String,
        description: String
    ): Flow<Resource<Organization>> {
        return organizationRepository.editOrganization(token, organizationId, name, description)
    }

    override fun updateOrganizationProfile(
        token: String,
        organizationId: Int,
        profilePic: String
    ): Flow<Resource<Organization>> {
        return organizationRepository.updateOrganizationProfile(token, organizationId, profilePic)
    }
}