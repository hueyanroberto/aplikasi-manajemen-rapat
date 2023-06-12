package ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserPreferenceRepository {
    fun getUser(): Flow<User>
    suspend fun saveUser(user: User)
    suspend fun logout()
    suspend fun saveName(user:User)
    suspend fun updateName(name: String)
    suspend fun updateProfile(profile: String)
}