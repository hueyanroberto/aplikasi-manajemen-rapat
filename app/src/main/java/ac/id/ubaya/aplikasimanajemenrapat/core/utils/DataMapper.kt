package ac.id.ubaya.aplikasimanajemenrapat.core.utils

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.UserData
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User

object DataMapper {
    fun userResponseToModel(userData: UserData): User {
        return User(
            id = userData.id,
            email = userData.email,
            name = userData.name,
            profilePic = userData.profilePic,
            exp = userData.exp,
            levelId = userData.levelId
        )
    }
}