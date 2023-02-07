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
                val userResponse = apiService.login(BuildConfig.API_KEY, email, password)
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
                val userResponse = apiService.register(BuildConfig.API_KEY, email, password)
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

    suspend fun registerNameAndProfilePic(
        userId: Int,
        name: String,
        profilePic: String
    ): Flow<ApiResponse<UserResponse>> {
        return flow {
            try {
                val userResponse = apiService.registerNameAndProfilePic(BuildConfig.API_KEY, userId, name, profilePic)
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