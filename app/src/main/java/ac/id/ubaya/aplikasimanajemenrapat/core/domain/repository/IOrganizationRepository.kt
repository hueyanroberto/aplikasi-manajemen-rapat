package ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import kotlinx.coroutines.flow.Flow

interface IOrganizationRepository {
    fun getListOrganization(token: String): Flow<Resource<List<Organization>>>
    fun getListFromDatabase(): Flow<List<Organization>>
    fun createOrganization(name: String, description: String, profilePic: String, token: String): Flow<Resource<Organization?>>
    fun joinOrganization(token: String, organizationCode: String): Flow<Resource<Organization?>>
}