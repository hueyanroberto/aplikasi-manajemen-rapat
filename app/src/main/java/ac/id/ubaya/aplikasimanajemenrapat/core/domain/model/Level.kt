package ac.id.ubaya.aplikasimanajemenrapat.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Level(
	val badgeUrl: String,
	val level: Int,
	val name: String,
	val id: Int,
	val minExp: Int,
	val maxExp: Int
): Parcelable
