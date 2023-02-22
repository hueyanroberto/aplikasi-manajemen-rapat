package ac.id.ubaya.aplikasimanajemenrapat.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val email: String,
    var name: String,
    var exp: Int,
    var profilePic: String?,
    var levelId: Int,
    var token: String? = null
): Parcelable
