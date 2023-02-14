package ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import kotlinx.coroutines.flow.Flow

interface IOrganizationRepository {
    fun getListOrganization(userId: Int): Flow<Resource<List<Organization>>>
    fun getListFromDatabase(): Flow<List<Organization>>
    fun createOrganization(name: String, description: String, profilePic: String, userId: Int): Flow<Resource<Organization?>>
    fun joinOrganization(userId: Int, organizationCode: String): Flow<Resource<Organization?>>
}