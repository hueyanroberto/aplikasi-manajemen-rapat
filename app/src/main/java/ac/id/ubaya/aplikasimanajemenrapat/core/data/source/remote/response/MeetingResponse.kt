package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.Date

@Parcelize
data class MeetingResponse(

	@field:SerializedName("data")
	val dataMeeting: List<MeetingItem>
) : Parcelable

@Parcelize
data class UpdateMeetingResponse(
	@field:SerializedName("data")
	val meeting: MeetingItem
): Parcelable

@Parcelize
data class MeetingItem(

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

	@field:SerializedName("status")
	val status: Int
) : Parcelable
