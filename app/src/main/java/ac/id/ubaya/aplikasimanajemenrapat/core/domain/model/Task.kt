package ac.id.ubaya.aplikasimanajemenrapat.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Task(
	val id: Int,
	val meetingId: Int,
	val description: String,
	val title: String,
	val deadline: Date,
	val user: String,
	val assignedTo: Int,
	val status: Int
): Parcelable
