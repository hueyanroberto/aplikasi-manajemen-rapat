package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.Date

@Parcelize
data class LeaderboardResponse(

	@field:SerializedName("data")
	val leaderboardData: LeaderboardData
) : Parcelable

@Parcelize
data class LeaderboardData(
	@field:SerializedName("start_date")
	val startDate: Date?,

	@field:SerializedName("end_date")
	val endDate: Date?,

	@field:SerializedName("duration")
	val duration: Int?,

	@field:SerializedName("period")
	val period: Int,

	@field:SerializedName("leaderboard")
	val leaderboard: List<LeaderboardItem>
): Parcelable

@Parcelize
data class LeaderboardItem(

	@field:SerializedName("points_get")
	val pointsGet: Int,

	@field:SerializedName("level_id")
	val levelId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("profile_pic")
	val profilePic: String,

	@field:SerializedName("id")
	val id: Int
) : Parcelable
