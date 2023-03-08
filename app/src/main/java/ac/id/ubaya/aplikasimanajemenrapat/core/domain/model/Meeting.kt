package ac.id.ubaya.aplikasimanajemenrapat.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Meeting(
    val startTime: Date,
    val code: String,
    val endTime: Date,
    val description: String,
    val location: String,
    val id: Int,
    val title: String,
    var status: Int,
    var userStatus: Int = -1,
    val userRole: Int = -1,
    val agenda: List<Agenda> = listOf(),
    val participant: List<Participant> = listOf(),
    val attachments: List<Attachment> = listOf()
): Parcelable

@Parcelize
data class Agenda(
    var task: String,
    val meetingId: Int,
    val id: Int,
    val completed: Int,
    val suggestions: List<Suggestion>? = null
): Parcelable

@Parcelize
data class Participant(
    val name: String,
    val profilePic: String,
    val id: Int,
    val email: String,
    val status: Int,
    val role: String
): Parcelable