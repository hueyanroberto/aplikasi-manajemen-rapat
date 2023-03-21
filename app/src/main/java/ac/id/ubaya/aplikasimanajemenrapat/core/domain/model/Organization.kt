package ac.id.ubaya.aplikasimanajemenrapat.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Organization(
    val id: Int,
    var name: String,
    val code: String,
    var description: String,
    val profilePicture: String,
    val leaderboardStart: String?,
    val leaderboardEnd: String?,
    val leaderboardPeriod: Int,
    var leaderboardDuration: Int,
    val role: Role? = null
): Parcelable
