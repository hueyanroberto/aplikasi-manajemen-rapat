package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.organization

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface OrganizationUseCase {
    fun getListOrganization(token: String): Flow<Resource<List<Organization>>>
    fun getListFromDatabase(): Flow<List<Organization>>
    fun createOrganization(name: String, description: String, profilePic: String, token: String): Flow<Resource<Organization?>>
    fun joinOrganization(token: String, organizationCode: String): Flow<Resource<Organization?>>
    fun getOrganizationMembers(token: String, organizationId: Int): Flow<Resource<List<User>>>
    fun updateRole(token: String, organizationId: Int, userId: Int, roleId: Int): Flow<Resource<User>>
}