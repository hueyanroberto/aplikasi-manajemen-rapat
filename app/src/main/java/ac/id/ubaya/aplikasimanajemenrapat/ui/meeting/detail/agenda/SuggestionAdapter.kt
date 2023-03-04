package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Suggestion
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemSuggestionBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SuggestionAdapter(
    private val listSuggestion: List<Suggestion>,
    private val role: Int
): RecyclerView.Adapter<SuggestionAdapter.ViewHolder>() {

    private lateinit var onItemClickedCallback: OnItemClickedCallback

    fun setOnItemClickedCallback(onItemClickedCallback: OnItemClickedCallback) {
        this.onItemClickedCallback = onItemClickedCallback
    }

    class ViewHolder(val binding: ItemSuggestionBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSuggestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listSuggestion.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listSuggestion[position]
        with(holder) {
            binding.textSuggestorName.text = data.user
            binding.textSuggestion.text = data.suggestion

            binding.textAcceptSuggestion.visibility = View.GONE

            if (role == 1) {
                binding.textAcceptSuggestion.visibility = View.VISIBLE
                if (data.accepted == 0) {
                    binding.textAcceptSuggestion.setBackgroundResource(R.drawable.rounded_pill_primary)
                    binding.textAcceptSuggestion.text = itemView.context.getString(R.string.accept)
                } else {
                    binding.textAcceptSuggestion.setBackgroundResource(R.drawable.rounded_pill_grey)
                    binding.textAcceptSuggestion.text = itemView.context.getString(R.string.accepted)
                }
                binding.textAcceptSuggestion.setOnClickListener {
                    onItemClickedCallback.onItemClickedCallback(data, adapterPosition)
                }
            }
        }
    }

    interface OnItemClickedCallback {
        fun onItemClickedCallback(data: Suggestion, position: Int)
    }
}