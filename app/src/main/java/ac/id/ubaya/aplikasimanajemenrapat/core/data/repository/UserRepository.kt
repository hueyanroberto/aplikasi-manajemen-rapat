package ac.id.ubaya.aplikasimanajemenrapat.core.data.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.datasource.UserRemoteDataSource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Achievement
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository.IUserRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.utils.DataMapper
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
): IUserRepository {
    override fun login(email: String, password: String): Flow<Resource<User?>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = userRemoteDataSource.login(email, password).first()) {
                is ApiResponse.Success -> {
                    val data = DataMapper.userResponseToModel(apiResponse.data.userData!!)
                    emit(Resource.Success(data))
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

    override fun loginGoogle(email: String): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = userRemoteDataSource.loginGoogle(email).first()) {
                is ApiResponse.Success -> {
                    val data = DataMapper.userResponseToModel(apiResponse.data.userData!!)
                    emit(Resource.Success(data))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("internal server error"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun register(email: String, password: String): Flow<Resource<User?>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = userRemoteDataSource.register(email, password).first()) {
                is ApiResponse.Success -> {
                    val data = DataMapper.userResponseToModel(apiResponse.data.userData!!)
                    emit(Resource.Success(data))
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

    override fun registerNameAndProfile(
        token: String,
        userId: Int,
        name: String,
        profilePic: String
    ): Flow<Resource<User?>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = userRemoteDataSource.registerNameAndProfilePic(token, userId, name, profilePic).first()) {
                is ApiResponse.Success -> {
                    val data = DataMapper.userResponseToModel(apiResponse.data.userData!!)
                    emit(Resource.Success(data))
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

    override suspend fun logout(token: String) {
        userRemoteDataSource.logout(token)
    }

    override suspend fun addFirebaseToken(token: String, firebaseToken: String) {
        userRemoteDataSource.insertFirebaseToken(token, firebaseToken)
    }

    override fun getProfile(token: String): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = userRemoteDataSource.getProfile(token).first()) {
                is ApiResponse.Success -> {
                    val data = DataMapper.userProfileToModel(apiResponse.data.profileData!!)
                    emit(Resource.Success(data))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("not found"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun updateProfile(token: String, name: String): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = userRemoteDataSource.updateProfile(token, name).first()) {
                is ApiResponse.Success -> {
                    val data = DataMapper.userProfileToModel(apiResponse.data.profileData!!)
                    emit(Resource.Success(data))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("not found"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun updateProfilePic(token: String, profilePic: String): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = userRemoteDataSource.updateProfilePic(token, profilePic).first()) {
                is ApiResponse.Success -> {
                    val data = DataMapper.userProfileToModel(apiResponse.data.profileData!!)
                    emit(Resource.Success(data))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("not found"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun getOtherProfile(token: String, userId: Int): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = userRemoteDataSource.getOtherProfile(token, userId).first()) {
                is ApiResponse.Success -> {
                    val data = DataMapper.userProfileToModel(apiResponse.data.profileData!!)
                    emit(Resource.Success(data))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("not found"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun getAchievements(token: String): Flow<Resource<List<Achievement>>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = userRemoteDataSource.getAchievements(token).first()) {
                is ApiResponse.Success -> {
                    val data = DataMapper.achievementResponseToModel(apiResponse.data.data)
                    emit(Resource.Success(data))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("not found"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }
}