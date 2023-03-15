package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class AchievementListResponse(

	@field:SerializedName("data")
	val data: List<AchievementItem>
) : Parcelable

@Parcelize
data class AchievementItem(

	@field:SerializedName("badge_url")
	val badgeUrl: String,

	@field:SerializedName("milestone")
	val milestone: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String?,

	@field:SerializedName("progress")
	val progress: Int?,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("reward_exp")
	val rewardExp: Int,

	@field:SerializedName("status")
	val status: Int?
) : Parcelable
