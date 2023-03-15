package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ProfileResponse(

	@field:SerializedName("data")
	val profileData: ProfileData?
) : Parcelable

@Parcelize
data class ProfileData(

	@field:SerializedName("level_id")
	val levelId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("profile_pic")
	val profilePic: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("exp")
	val exp: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("level")
	val level: LevelResponse,

	@field:SerializedName("achievement")
	val achievement: List<AchievementItem>,
) : Parcelable
