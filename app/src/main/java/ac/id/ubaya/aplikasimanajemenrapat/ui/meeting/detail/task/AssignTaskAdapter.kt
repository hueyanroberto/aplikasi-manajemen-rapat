package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.task

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Participant
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemSelectParticipantBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class AssignTaskAdapter(
    private val listParticipant: List<Participant>
): RecyclerView.Adapter<AssignTaskAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemLongClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(val binding: ItemSelectParticipantBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSelectParticipantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listParticipant.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listParticipant[position]
        with(holder) {
            binding.checkSelectParticipant.visibility = View.GONE
            binding.textSelectParticipantName.text = data.name
            binding.textSelectParticipantRole.text = data.role

            Glide.with(itemView.context)
                .load("$BASE_ASSET_URL/Profile/user/${data.profilePic}")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.blank_profile)
                .into(binding.imageSelectParticipant)

            itemView.setOnClickListener {
                onItemClickCallback.onItemClickCallback(data)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClickCallback(participant: Participant)
    }
}