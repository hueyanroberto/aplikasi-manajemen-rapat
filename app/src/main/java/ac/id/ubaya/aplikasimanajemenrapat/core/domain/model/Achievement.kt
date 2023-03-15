package ac.id.ubaya.aplikasimanajemenrapat.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Achievement(
	val badgeUrl: String,
	val milestone: Int,
	val name: String,
	val description: String = "",
	val progress: Int = 0,
	val id: Int,
	val rewardExp: Int,
	val status: Int = 0
): Parcelable
