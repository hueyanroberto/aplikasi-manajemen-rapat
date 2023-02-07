package ac.id.ubaya.aplikasimanajemenrapat.core.data.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.datasource.UserRemoteDataSource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository.IUserRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.utils.DataMapper
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
        userId: Int,
        name: String,
        profilePic: String
    ): Flow<Resource<User?>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = userRemoteDataSource.registerNameAndProfilePic(userId, name, profilePic).first()) {
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
}