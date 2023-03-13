package ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IOrganizationRepository {
    fun getListOrganization(token: String): Flow<Resource<List<Organization>>>
    fun getListFromDatabase(): Flow<List<Organization>>
    fun createOrganization(name: String, description: String, profilePic: String, token: String, duration: Int): Flow<Resource<Organization?>>
    fun joinOrganization(token: String, organizationCode: String): Flow<Resource<Organization?>>
    fun getOrganizationMembers(token: String, organizationId: Int): Flow<Resource<List<User>>>
    fun updateRole(token: String, organizationId: Int, userId: Int, roleId: Int): Flow<Resource<User>>
    fun editOrganization(token: String, organizationId: Int, name: String, description: String): Flow<Resource<Organization>>
    fun updateOrganizationProfile(token: String, organizationId: Int, profilePic: String): Flow<Resource<Organization>>
}