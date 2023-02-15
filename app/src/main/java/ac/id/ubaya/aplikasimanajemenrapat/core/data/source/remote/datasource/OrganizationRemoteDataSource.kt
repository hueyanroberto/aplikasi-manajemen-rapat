package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.datasource

import ac.id.ubaya.aplikasimanajemenrapat.BuildConfig
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiService
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.OrganizationResponse
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrganizationRemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getListOrganization(userId: Int): Flow<ApiResponse<OrganizationResponse>> {
        return flow {
            try {
                val organizationResponse = apiService.getListOrganization(BuildConfig.API_KEY, userId)
                if (organizationResponse.organizationData.isNotEmpty()) {
                    emit(ApiResponse.Success(organizationResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("OrganizationRDataSource", "getListOrganization: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun createOrganization(
        name: String,
        description: String,
        profilePic: String,
        userId: Int
    ): Flow<ApiResponse<OrganizationResponse>> {
        return flow {
            try {
                val organizationResponse = apiService.createOrganization(BuildConfig.API_KEY, name, description, profilePic, userId)
                if (organizationResponse.organizationData.isNotEmpty()) {
                    emit(ApiResponse.Success(organizationResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("OrganizationRDataSource", "createOrganization: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun joinOrganization(userId: Int, organizationCode: String): Flow<ApiResponse<OrganizationResponse>> {
        return flow {
            try {
                val organizationResponse = apiService.joinOrganization(BuildConfig.API_KEY, userId, organizationCode)
                if (organizationResponse.organizationData.isNotEmpty()) {
                    emit(ApiResponse.Success(organizationResponse))
                } else {
                    if (organizationResponse.status == "already joined") {
                        emit(ApiResponse.Empty)
                    } else if (organizationResponse.status == "not found") {
                        emit(ApiResponse.Error(organizationResponse.status))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("OrganizationRDataSource", "joinOrganization: $e")
            }
        }.flowOn(Dispatchers.IO)
    }
}