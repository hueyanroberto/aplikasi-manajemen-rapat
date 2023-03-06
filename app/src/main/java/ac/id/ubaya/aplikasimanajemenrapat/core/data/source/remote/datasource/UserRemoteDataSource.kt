package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.datasource

import ac.id.ubaya.aplikasimanajemenrapat.BuildConfig
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiService
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.UserResponse
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(private val apiService: ApiService) {

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
}