package ac.id.ubaya.aplikasimanajemenrapat.core.data.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.local.datasource.OrganizationLocalDataSource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.datasource.OrganizationRemoteDataSource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository.IOrganizationRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrganizationRepository @Inject constructor(
    private val organizationRemoteDataSource: OrganizationRemoteDataSource,
    private val organizationLocalDataSource: OrganizationLocalDataSource
) : IOrganizationRepository {
    override fun getListOrganization(userId: Int): Flow<Resource<List<Organization>>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = organizationRemoteDataSource.getListOrganization(userId).first()) {
                is ApiResponse.Success -> {
                    val organizationList = apiResponse.data.organizationData
                    organizationLocalDataSource.deleteAll()
                    organizationLocalDataSource.insert(DataMapper.organizationResponseToEntity(organizationList))

                    val organization = organizationLocalDataSource.getOrganizations().first()
                    emit(Resource.Success(DataMapper.organizationEntityToModel(organization)))
                }
                is ApiResponse.Empty -> {
                    organizationLocalDataSource.deleteAll()
                    emit(Resource.Success(listOf<Organization>()))
                }
                is ApiResponse.Error -> {
                    val organization = organizationLocalDataSource.getOrganizations().first()
                    emit(Resource.Error(apiResponse.errorMessage, DataMapper.organizationEntityToModel(organization)))
                }
            }
        }
    }

    override fun getListFromDatabase(): Flow<List<Organization>> {
        return organizationLocalDataSource.getOrganizations().map { DataMapper.organizationEntityToModel(it) }
    }

    override fun createOrganization(
        name: String,
        description: String,
        profilePic: String,
        userId: Int
    ): Flow<Resource<Organization?>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = organizationRemoteDataSource.createOrganization(name, description, profilePic, userId).first()) {
                is ApiResponse.Success -> {
                    val organizationList = apiResponse.data.organizationData
                    organizationLocalDataSource.insert(DataMapper.organizationResponseToEntity(organizationList))

                    emit(Resource.Success(DataMapper.organizationResponseToModel(organizationList)[0]))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(null))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun joinOrganization(
        userId: Int,
        organizationCode: String
    ): Flow<Resource<Organization?>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = organizationRemoteDataSource.joinOrganization(userId, organizationCode).first()) {
                is ApiResponse.Success -> {
                    val organizationList = apiResponse.data.organizationData
                    organizationLocalDataSource.insert(DataMapper.organizationResponseToEntity(organizationList))

                    emit(Resource.Success(DataMapper.organizationResponseToModel(organizationList)[0]))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(null))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }
}