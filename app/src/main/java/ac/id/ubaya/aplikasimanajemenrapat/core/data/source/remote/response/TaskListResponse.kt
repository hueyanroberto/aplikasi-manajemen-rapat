package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.Date

@Parcelize
data class TaskListResponse(

	@field:SerializedName("data")
	val listTask: List<TaskItem>
) : Parcelable


@Parcelize
data class TaskResponse(

	@field:SerializedName("data")
	val task: TaskItem?
) : Parcelable

@Parcelize
data class TaskItem(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("meeting_id")
	val meetingId: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("deadline")
	val deadline: Date,

	@field:SerializedName("user")
	val user: String,

	@field:SerializedName("assigned_to")
	val assignedTo: Int,

	@field:SerializedName("status")
	val status: Int
) : Parcelable
