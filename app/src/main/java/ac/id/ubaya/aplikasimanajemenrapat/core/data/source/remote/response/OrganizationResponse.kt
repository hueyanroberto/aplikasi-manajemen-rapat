package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.Date

@Parcelize
data class OrganizationResponse(

	@field:SerializedName("data")
	val organizationData: List<OrganizationData>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
) : Parcelable

@Parcelize
data class OrganizationData(

	@field:SerializedName("profilePicture")
	val profilePicture: String,

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("leaderboardEnd")
	val leaderboardEnd: Date?,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("leaderboardStart")
	val leaderboardStart: Date?
) : Parcelable
