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
    private val role: Int,
    private val meetingStatus: Int,
    private var agendaStatus: Int = 0
): RecyclerView.Adapter<SuggestionAdapter.ViewHolder>() {

    private lateinit var onItemClickedCallback: OnItemClickedCallback

    fun setOnItemClickedCallback(onItemClickedCallback: OnItemClickedCallback) {
        this.onItemClickedCallback = onItemClickedCallback
    }

    fun updateAgendaStatus(agendaStatus: Int) {
        this.agendaStatus = agendaStatus
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

            val status = if (data.accepted == 0) itemView.context.getString(R.string.not_yet_accepted)
                            else itemView.context.getString(R.string.accepted)
            binding.textSuggestionStatus.text = itemView.context.getString(R.string.suggestion_status, status)

            binding.textAcceptSuggestion.visibility = View.GONE
            binding.textDeleteSuggestion.visibility = View.GONE
            if (role == 1 && meetingStatus == 1) {
                if (agendaStatus == 0) {
                    binding.textAcceptSuggestion.visibility = View.VISIBLE
                    if (data.accepted == 0) {
                        binding.textAcceptSuggestion.setBackgroundResource(R.drawable.rounded_pill_primary)
                        binding.textAcceptSuggestion.text = itemView.context.getString(R.string.accept)

                        binding.textDeleteSuggestion.visibility = View.VISIBLE
                        binding.textDeleteSuggestion.setOnClickListener {
                            onItemClickedCallback.onDeleteClickedCallback(data, adapterPosition)
                        }
                    } else {
                        binding.textAcceptSuggestion.setBackgroundResource(R.drawable.rounded_pill_grey)
                        binding.textAcceptSuggestion.text = itemView.context.getString(R.string.accepted)

                        binding.textDeleteSuggestion.visibility = View.GONE
                        binding.textDeleteSuggestion.setOnClickListener(null)
                    }
                    binding.textAcceptSuggestion.setOnClickListener {
                        onItemClickedCallback.onItemClickedCallback(data, adapterPosition)
                    }
                } else {
                    binding.textAcceptSuggestion.visibility = View.GONE
                    binding.textDeleteSuggestion.visibility = View.GONE
                }
            }
        }
    }

    interface OnItemClickedCallback {
        fun onItemClickedCallback(data: Suggestion, position: Int)
        fun onDeleteClickedCallback(data: Suggestion, position: Int)
    }
}