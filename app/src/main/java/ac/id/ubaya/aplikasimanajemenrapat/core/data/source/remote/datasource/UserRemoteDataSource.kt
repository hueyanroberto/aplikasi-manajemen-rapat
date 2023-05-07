package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.datasource

import ac.id.ubaya.aplikasimanajemenrapat.BuildConfig
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiService
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.AchievementListResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.ProfileResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.UserResponse
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun login (email: String, password: String): Flow<ApiResponse<UserResponse>> {
        return flow {
            try {
                val userResponse = apiService.login(email, password)
                if (userResponse.userData != null) {
                    emit(ApiResponse.Success(userResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("UserRDataSource", "login: $e" )
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun loginGoogle (email: String): Flow<ApiResponse<UserResponse>> {
        return flow {
            try {
                val userResponse = apiService.loginGoogle(email)
                if (userResponse.userData != null) {
                    emit(ApiResponse.Success(userResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("UserRDataSource", "loginGoogle: $e" )
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun register (email: String, password: String): Flow<ApiResponse<UserResponse>> {
        return flow {
            try {
                val userResponse = apiService.register(email, password)
                if (userResponse.userData != null) {
                    emit(ApiResponse.Success(userResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("UserRDataSource", "register: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun logout (token: String) {
        apiService.logout("Bearer $token")
    }

    suspend fun insertFirebaseToken(token: String, firebaseToken: String) {
        apiService.insertFirebaseToken("Bearer $token", firebaseToken)
    }

    suspend fun registerNameAndProfilePic(
        token: String,
        userId: Int,
        name: String,
        profilePic: String,
    ): Flow<ApiResponse<UserResponse>> {
        return flow {
            try {
                val userResponse = apiService.registerNameAndProfilePic("Bearer $token", name, profilePic)
                if (userResponse.userData != null) {
                    emit(ApiResponse.Success(userResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("UserRDataSource", "registerNameAndProfile: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getProfile(token: String): Flow<ApiResponse<ProfileResponse>> {
        return flow {
            try {
                val userResponse = apiService.getProfile("Bearer $token")
                if (userResponse.profileData != null) {
                    emit(ApiResponse.Success(userResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("UserRDataSource", "getProfile: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateProfile(token: String, name: String): Flow<ApiResponse<ProfileResponse>> {
        return flow {
            try {
                val userResponse = apiService.updateProfile("Bearer $token", name)
                if (userResponse.profileData != null) {
                    emit(ApiResponse.Success(userResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("UserRDataSource", "updateProfile: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateProfilePic(token: String, profilePic: String): Flow<ApiResponse<ProfileResponse>> {
        return flow {
            try {
                val userResponse = apiService.updateProfilePic("Bearer $token", profilePic)
                if (userResponse.profileData != null) {
                    emit(ApiResponse.Success(userResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("UserRDataSource", "updateProfilePic: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getOtherProfile(token: String, userId: Int): Flow<ApiResponse<ProfileResponse>> {
        return flow {
            try {
                val userResponse = apiService.getOtherProfile("Bearer $token", userId)
                if (userResponse.profileData != null) {
                    emit(ApiResponse.Success(userResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("UserRDataSource", "getOtherProfile: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAchievements(token: String): Flow<ApiResponse<AchievementListResponse>> {
        return flow {
            try {
                val userResponse = apiService.getAchievements("Bearer $token")
                if (userResponse.data.isNotEmpty()) {
                    emit(ApiResponse.Success(userResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("UserRDataSource", "getAchievements: $e")
            }
        }.flowOn(Dispatchers.IO)
    }
}