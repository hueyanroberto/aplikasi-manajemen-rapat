package ac.id.ubaya.aplikasimanajemenrapat.ui.organization.memberList

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemPersonBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MemberListAdapter(private val members: List<User>): RecyclerView.Adapter<MemberListAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallBack

    fun setOnItemLongClickCallback(onItemClickCallback: OnItemClickCallBack) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = members.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = members[position]
        holder.bind(data)

        holder.itemView.setOnLongClickListener {
            onItemClickCallback.onItemLongClickCallback(data, holder.adapterPosition)
            true
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemPersonBinding.bind(itemView)
        fun bind(data: User) {
            binding.textPersonName.text = data.name
            binding.textPersonRole.text = data.role?.name

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

    interface OnItemClickCallBack {
        fun onItemLongClickCallback(user: User, position: Int)
    }
}