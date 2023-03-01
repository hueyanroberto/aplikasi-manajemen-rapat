package ac.id.ubaya.aplikasimanajemenrapat.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Suggestion(
    val userId: Int,
    val suggestion: String,
    var accepted: Int,
    val id: Int,
    val agendaId: Int,
    val user: String
) : Parcelable
