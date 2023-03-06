package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.Date

@Parcelize
data class MeetingDetailResponse(

	@field:SerializedName("data")
	val meetingDetailData: MeetingDetailData? = null
) : Parcelable

@Parcelize
data class MeetingDetailData(

	@field:SerializedName("start_time")
	val startTime: Date,

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("end_time")
	val endTime: Date,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("agenda")
	val agenda: List<AgendaItem>,

	@field:SerializedName("participant")
	val participant: List<ParticipantItem>,

	@field:SerializedName("status")
	val status: Int,

	@field:SerializedName("user_status")
	val userStatus: Int,

	@field:SerializedName("user_role")
	val userRole: Int
) : Parcelable

@Parcelize
data class AgendaItem(

	@field:SerializedName("task")
	val task: String,

	@field:SerializedName("meeting_id")
	val meetingId: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("completed")
	val completed: Int,

	@field:SerializedName("suggestions")
	val suggestions: List<SuggestionItem>? = null
) : Parcelable

@Parcelize
data class ParticipantItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("profile_pic")
	val profilePic: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("status")
	val status: Int,

	@field:SerializedName("role")
	val role: String
) : Parcelable


@Parcelize
data class AgendaResponse(
	@field:SerializedName("data")
	val agendaData: List<AgendaItem>
): Parcelable