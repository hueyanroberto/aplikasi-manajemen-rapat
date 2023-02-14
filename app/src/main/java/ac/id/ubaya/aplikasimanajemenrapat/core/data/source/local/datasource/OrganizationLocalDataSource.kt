package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.local.datasource

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.local.entity.OrganizationEntity
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.local.room.OrganizationDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrganizationLocalDataSource @Inject constructor(private val organizationDao: OrganizationDao) {
    fun getOrganizations(): Flow<List<OrganizationEntity>> = organizationDao.getOrganizations()
    suspend fun deleteAll() = organizationDao.deleteAll()
    suspend fun updateOrganization(organization: OrganizationEntity) = organizationDao.update(organization)
    suspend fun insert(organizationEntity: List<OrganizationEntity>) = organizationDao.insert(organizationEntity)
}