package ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Achievement
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun login(email: String, password: String): Flow<Resource<User?>>
    fun loginGoogle(email: String): Flow<Resource<User>>
    fun register(email: String, password: String): Flow<Resource<User?>>
    suspend fun logout(token: String)
    suspend fun addFirebaseToken(token: String, firebaseToken: String)
    fun registerNameAndProfile(token: String, userId: Int, name: String, profilePic: String): Flow<Resource<User?>>
    fun getProfile(token: String): Flow<Resource<User>>
    fun updateProfile(token: String, name: String): Flow<Resource<User>>
    fun getOtherProfile(token: String, userId: Int): Flow<Resource<User>>
    fun getAchievements(token: String): Flow<Resource<List<Achievement>>>
}