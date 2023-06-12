package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.userPreference

import ac.id.ubaya.aplikasimanajemenrapat.core.data.repository.UserPreferenceRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferenceInteractor @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
): UserPreferenceUseCase {
    override fun getUser(): Flow<User> = userPreferenceRepository.getUser()
    override suspend fun saveUser(user: User) = userPreferenceRepository.saveUser(user)
    override suspend fun logout() = userPreferenceRepository.logout()
    override suspend fun saveName(user: User) = userPreferenceRepository.saveName(user)
    override suspend fun updateName(name: String) = userPreferenceRepository.updateName(name)
    override suspend fun updateProfile(profile: String) = userPreferenceRepository.updateProfile(profile)
}