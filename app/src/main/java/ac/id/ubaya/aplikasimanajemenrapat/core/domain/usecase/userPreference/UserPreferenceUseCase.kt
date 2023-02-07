package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.userPreference

import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserPreferenceUseCase {
    fun getUser(): Flow<User>
    suspend fun saveUser(user: User)
    suspend fun logout()
}