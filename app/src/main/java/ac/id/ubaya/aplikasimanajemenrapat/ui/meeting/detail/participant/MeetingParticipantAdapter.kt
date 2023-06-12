package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.participant

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Participant
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemPersonBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MeetingParticipantAdapter(
    private val listParticipant: List<Participant>
): RecyclerView.Adapter<MeetingParticipantAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemLongClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(val binding: ItemPersonBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listParticipant.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listParticipant[position]
        with(holder) {
            binding.textPersonName.text = data.name

            when (data.role) {
                "1" -> binding.textPersonRole.text = itemView.context.getString(R.string.leader)
                "2" -> binding.textPersonRole.text = itemView.context.getString(R.string.participant)
            }

            when (data.status) {
                0 -> binding.textPersonStatus.text = itemView.context.getString(R.string.suggestion_status, itemView.context.getString(R.string.not_yet_present))
                1 -> binding.textPersonStatus.text = itemView.context.getString(R.string.suggestion_status, itemView.context.getString(R.string.present))
            }

            Glide.with(itemView.context)
                .load("$BASE_ASSET_URL/Profile/User/${data.profilePic}")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.blank_profile)
                .into(binding.imagePersonProfile)

            binding.imagePersonMail.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    this.data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(data.email))
                }
                itemView.context.startActivity(intent)
            }

            itemView.setOnClickListener { onItemClickCallback.onItemClickCallback(data) }

            when (data.levelId) {
                1 -> binding.imagePersonLevelBadge.setBackgroundResource(R.drawable.badges_02)
                2 -> binding.imagePersonLevelBadge.setBackgroundResource(R.drawable.badges_03)
                3 -> binding.imagePersonLevelBadge.setBackgroundResource(R.drawable.badges_04)
                4 -> binding.imagePersonLevelBadge.setBackgroundResource(R.drawable.badges_05)
                5 -> binding.imagePersonLevelBadge.setBackgroundResource(R.drawable.badges_06)
                6 -> binding.imagePersonLevelBadge.setBackgroundResource(R.drawable.badges_07)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClickCallback(participant: Participant)
    }
}