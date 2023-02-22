package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoleResponse(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
): Parcelable
