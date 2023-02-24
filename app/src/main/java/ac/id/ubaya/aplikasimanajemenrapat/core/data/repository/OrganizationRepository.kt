package ac.id.ubaya.aplikasimanajemenrapat.core.data.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.local.datasource.OrganizationLocalDataSource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.datasource.OrganizationRemoteDataSource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository.IOrganizationRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.utils.DataMapper
import android.util.Log
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
    override fun getListOrganization(token: String): Flow<Resource<List<Organization>>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = organizationRemoteDataSource.getListOrganization(token).first()) {
                is ApiResponse.Success -> {
                    val organizationList = apiResponse.data.organizationData
                    Log.d("OrganizationRepository", organizationList.toString())
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
        token: String
    ): Flow<Resource<Organization?>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = organizationRemoteDataSource.createOrganization(name, description, profilePic, token).first()) {
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
        token: String,
        organizationCode: String
    ): Flow<Resource<Organization?>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = organizationRemoteDataSource.joinOrganization(token, organizationCode).first()) {
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

    override fun getOrganizationMembers(
        token: String,
        organizationId: Int
    ): Flow<Resource<List<User>>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = organizationRemoteDataSource.getOrganizationMembers(token, organizationId).first()) {
                is ApiResponse.Success -> {
                    val userList = apiResponse.data.userData
                    emit(Resource.Success(DataMapper.userResponseToModel(userList)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf<User>()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }
}