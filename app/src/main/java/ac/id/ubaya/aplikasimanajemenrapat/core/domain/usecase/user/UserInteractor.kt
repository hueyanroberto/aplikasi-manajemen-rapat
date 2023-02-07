package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.user

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.repository.UserRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserInteractor @Inject constructor(private val userRepository: UserRepository): UserUseCase {
    override fun login(email: String, password: String): Flow<Resource<User?>> {
        return userRepository.login(email, password)
    }

    override fun register(email: String, password: String): Flow<Resource<User?>> {
        return userRepository.register(email, password)
    }

    override fun registerNameAndProfilePic(userId: Int, name: String, profilePic: String) {
        TODO("Not yet implemented")
    }
}