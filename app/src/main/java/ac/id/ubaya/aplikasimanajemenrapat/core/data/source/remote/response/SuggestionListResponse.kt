package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SuggestionListResponse(

	@field:SerializedName("data")
	val suggestionItemList: List<SuggestionItem>
) : Parcelable

@Parcelize
data class SuggestionResponse(
	@field:SerializedName("data")
	val suggestionItem: SuggestionItem?
) : Parcelable

@Parcelize
data class SuggestionItem(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("suggestion")
	val suggestion: String,

	@field:SerializedName("accepted")
	val accepted: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("agenda_id")
	val agendaId: Int,

	@field:SerializedName("user")
	val user: String
) : Parcelable
