package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LevelResponse(

	@field:SerializedName("badge_url")
	val badgeUrl: String,

	@field:SerializedName("level")
	val level: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("min_exp")
	val minExp: Int = 0,

	@field:SerializedName("max_exp")
	val maxExp: Int = 0,
) : Parcelable
