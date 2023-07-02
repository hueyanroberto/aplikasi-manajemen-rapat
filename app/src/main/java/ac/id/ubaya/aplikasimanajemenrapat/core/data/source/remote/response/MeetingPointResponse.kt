package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MeetingPointResponse(

	@field:SerializedName("data")
	val data: List<MeetingPointItem>
) : Parcelable

@Parcelize
data class MeetingPointItem(

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("point")
	val point: Int
) : Parcelable
