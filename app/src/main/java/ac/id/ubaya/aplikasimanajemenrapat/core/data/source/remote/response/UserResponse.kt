package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UserResponse(

	@field:SerializedName("data")
	val userData: UserData? = null,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
) : Parcelable

@Parcelize
data class UserData(

	@field:SerializedName("profilePic")
	val profilePic: String? = null,

	@field:SerializedName("levelId")
	val levelId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("exp")
	val exp: Int,

	@field:SerializedName("email")
	val email: String
) : Parcelable
