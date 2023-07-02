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
    var agenda: List<Agenda> = listOf(),
    var participant: List<Participant> = listOf(),
    var attachments: List<Attachment> = listOf(),
    val meetingNote: String = "",
    val realStart: Date? = null,
    val realEnd: Date? = null,
    val point: Int = 0
): Parcelable

@Parcelize
data class Agenda(
    var task: String,
    val meetingId: Int,
    val id: Int,
    var completed: Int,
    val suggestions: List<Suggestion>? = null
): Parcelable

@Parcelize
data class Participant(
    val name: String,
    val profilePic: String,
    val id: Int,
    val email: String,
    val status: Int,
    val role: String,
    val levelId: Int
): Parcelable

@Parcelize
data class MeetingPoint(
    val point: Int,
    val description: String
): Parcelable