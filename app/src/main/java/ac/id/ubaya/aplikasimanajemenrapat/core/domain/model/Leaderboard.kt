package ac.id.ubaya.aplikasimanajemenrapat.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Leaderboard(
    val pointsGet: Int,
    val levelId: Int,
    val name: String,
    val profilePic: String,
    val id: Int
): Parcelable

@Parcelize
data class LeaderboardDetail(
    val startDate: Date,
    val endDate: Date,
    val duration: Int,
    val period: Int,
    val leaderboards: List<Leaderboard>
): Parcelable
