package ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun login(email: String, password: String): Flow<Resource<User?>>
    fun register(email: String, password: String): Flow<Resource<User?>>
    fun registerNameAndProfile(userId: Int, name: String, profilePic: String): Flow<Resource<User?>>
}