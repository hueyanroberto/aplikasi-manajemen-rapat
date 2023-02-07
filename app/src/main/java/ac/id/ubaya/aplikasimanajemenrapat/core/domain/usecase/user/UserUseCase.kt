package ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.user

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    fun login(email: String, password: String): Flow<Resource<User?>>
    fun register (email: String, password: String): Flow<Resource<User?>>
    fun registerNameAndProfilePic (userId: Int, name: String, profilePic: String)
}