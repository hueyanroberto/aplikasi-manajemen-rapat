package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.user

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Achievement
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    fun login(email: String, password: String): Flow<Resource<User?>>
    fun register (email: String, password: String): Flow<Resource<User?>>
    suspend fun logout(token: String)
    fun registerNameAndProfilePic (token: String, userId: Int, name: String, profilePic: String): Flow<Resource<User?>>
    fun getProfile(token: String): Flow<Resource<User>>
    fun getAchievements(token: String): Flow<Resource<List<Achievement>>>
}