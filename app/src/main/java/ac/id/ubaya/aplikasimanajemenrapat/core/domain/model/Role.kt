package ac.id.ubaya.aplikasimanajemenrapat.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Role(
    val name: String,
    val id: Int
):Parcelable
