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
            binding.textPersonRole.text = data.role

            Glide.with(itemView.context)
                .load("$BASE_ASSET_URL/Profile/user/${data.profilePic}")
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
        }
    }
}