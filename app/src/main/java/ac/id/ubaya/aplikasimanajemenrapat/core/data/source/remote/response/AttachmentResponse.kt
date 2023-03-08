package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class AttachmentResponse(

	@field:SerializedName("data")
	val listAttachment: List<AttachmentItem>
) : Parcelable

@Parcelize
data class AttachmentItem(

	@field:SerializedName("meeting_id")
	val meetingId: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("url")
	val url: String
) : Parcelable
