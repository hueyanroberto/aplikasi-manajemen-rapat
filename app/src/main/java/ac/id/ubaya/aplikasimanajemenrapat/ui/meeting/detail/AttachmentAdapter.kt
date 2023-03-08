package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail

import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Attachment
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemAttachmentBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AttachmentAdapter (private val listAttachment: List<Attachment>): RecyclerView.Adapter<AttachmentAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemAttachmentBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttachmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder((binding))
    }

    override fun getItemCount(): Int = listAttachment.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listAttachment[position]
        with(holder) {
            binding.textAttachmentName.text = data.url
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("$BASE_ASSET_URL/File/${data.meetingId}/${data.url}")
                val chooser = Intent.createChooser(intent, "Open with")
                itemView.context.startActivity(chooser)
            }
        }
    }

}