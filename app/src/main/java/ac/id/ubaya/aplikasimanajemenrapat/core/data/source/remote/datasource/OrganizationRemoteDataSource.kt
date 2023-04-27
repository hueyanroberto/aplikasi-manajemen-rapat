package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.datasource

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiService
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.LeaderboardResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.OrganizationResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.UserListResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.UserResponse
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrganizationRemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getListOrganization(token: String): Flow<ApiResponse<OrganizationResponse>> {
        return flow {
            try {
                val organizationResponse = apiService.getListOrganization("Bearer $token")
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
        token: String,
        duration: Int
    ): Flow<ApiResponse<OrganizationResponse>> {
        return flow {
            try {
                val organizationResponse = apiService.createOrganization("Bearer $token", name, description, profilePic, duration)
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

    suspend fun joinOrganization(token: String, organizationCode: String): Flow<ApiResponse<OrganizationResponse>> {
        return flow {
            try {
                val organizationResponse = apiService.joinOrganization("Bearer $token", organizationCode)
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

    suspend fun getOrganizationMembers(token: String, organizationId: Int): Flow<ApiResponse<UserListResponse>> {
        return flow {
            try {
                val userListResponse = apiService.getOrganizationMembers("Bearer $token", organizationId)
                if (userListResponse.userData.isNotEmpty()) {
                    emit(ApiResponse.Success(userListResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("OrganizationRDataSource", "getOrganizationMembers: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateRole(
        token: String,
        organizationId: Int,
        userId: Int,
        roleId: Int
    ): Flow<ApiResponse<UserResponse>> {
        return flow {
            try {
                val userResponse = apiService.updateRole("Bearer $token", organizationId, userId, roleId)
                if (userResponse.userData != null) {
                    emit(ApiResponse.Success(userResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("OrganizationRDataSource", "updateRole: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateOrganizationProfilePic(token: String, organizationId: Int, profilePic: String): Flow<ApiResponse<OrganizationResponse>> {
        return flow {
            try {
                val organizationResponse = apiService.updateOrganizationProfilePic("Bearer $token", organizationId, profilePic)
                if (organizationResponse.organizationData.isNotEmpty()) {
                    emit(ApiResponse.Success(organizationResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("OrganizationRDataSource", "updateOrganizationProfilePic: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun editOrganization(token: String, organizationId: Int, name: String, description: String, duration: Int): Flow<ApiResponse<OrganizationResponse>> {
        return flow {
            try {
                val organizationResponse = apiService.editOrganization("Bearer $token", organizationId, name, description, duration)
                if (organizationResponse.organizationData.isNotEmpty()) {
                    emit(ApiResponse.Success(organizationResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("OrganizationRDataSource", "editOrganization: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getLeaderboard(token: String, organizationId: Int): Flow<ApiResponse<LeaderboardResponse>> {
        return flow {
            try {
                val leaderboardResponse = apiService.getLeaderboard("Bearer $token", organizationId)
                if (leaderboardResponse.leaderboardData.leaderboard.isNotEmpty()) {
                    emit(ApiResponse.Success(leaderboardResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("OrganizationRDataSource", "getLeaderboard: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getLeaderboardHistory(token: String, organizationId: Int, period: Int): Flow<ApiResponse<LeaderboardResponse>> {
        return flow {
            try {
                val leaderboardResponse = apiService.getLeaderboardHistory("Bearer $token", organizationId, period)
                emit(ApiResponse.Success(leaderboardResponse))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("OrganizationRDataSource", "getLeaderboardHistory: $e")
            }
        }.flowOn(Dispatchers.IO)
    }
}