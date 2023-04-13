package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.user

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.repository.UserRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Achievement
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserInteractor @Inject constructor(private val userRepository: UserRepository): UserUseCase {
    override fun login(email: String, password: String): Flow<Resource<User?>> =
        userRepository.login(email, password)

    override fun loginGoogle(email: String): Flow<Resource<User>> = userRepository.loginGoogle(email)

    override fun register(email: String, password: String): Flow<Resource<User?>> =
        userRepository.register(email, password)

    override suspend fun logout(token: String) = userRepository.logout(token)

    override suspend fun addFirebaseToken(token: String, firebaseToken: String) = userRepository.addFirebaseToken(token, firebaseToken)

    override fun registerNameAndProfilePic(token: String, userId: Int, name: String, profilePic: String) =
        userRepository.registerNameAndProfile(token, userId, name, profilePic)

    override fun getProfile(token: String): Flow<Resource<User>> = userRepository.getProfile(token)
    override fun updateProfile(token: String, name: String): Flow<Resource<User>> = userRepository.updateProfile(token, name)
    override fun getOtherProfile(token: String, userId: Int): Flow<Resource<User>> = userRepository.getOtherProfile(token, userId)
    override fun getAchievements(token: String): Flow<Resource<List<Achievement>>> = userRepository.getAchievements(token)
}