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
    val status: Int
): Parcelable
