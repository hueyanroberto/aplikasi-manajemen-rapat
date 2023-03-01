package ac.id.ubaya.aplikasimanajemenrapat.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Organization(
    val id: Int,
    val name: String,
    val code: String,
    val description: String,
    val profilePicture: String,
    val leaderboardStart: String?,
    val leaderboardEnd: String?,
    val role: Role? = null
): Parcelable
