package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UserResponse(
	@field:SerializedName("status")
	val status: String = "",

	@field:SerializedName("data")
	val userData: UserData? = null
) : Parcelable

@Parcelize
data class UserListResponse (

	@field:SerializedName("data")
	val userData: List<UserData>

) : Parcelable

@Parcelize
data class UserData(

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

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("level")
	val level: LevelResponse,

	@field:SerializedName("role")
	val role: RoleResponse? = null,

	@field:SerializedName("status")
	val statusLogin: Int? = null
) : Parcelable
