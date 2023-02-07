package ac.id.ubaya.aplikasimanajemenrapat.core.domain.model

data class User(
    val id: Int,
    val email: String,
    var name: String,
    var exp: Int,
    var profilePic: String?,
    var levelId: Int
)
