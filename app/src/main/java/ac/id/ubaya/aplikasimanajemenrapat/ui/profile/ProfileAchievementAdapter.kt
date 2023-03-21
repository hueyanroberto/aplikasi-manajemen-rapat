package ac.id.ubaya.aplikasimanajemenrapat.ui.profile

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Achievement
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemProfileAchievementBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.Locale

class ProfileAchievementAdapter (
    private val listAchievement: List<Achievement>
): RecyclerView.Adapter<ProfileAchievementAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemProfileAchievementBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProfileAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listAchievement.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listAchievement[position]
        with(holder) {
            val name = data.name
            val split = name.split("||")

            binding.textProfileAchievementName.text =
                if (Locale.getDefault().language == "id")
                    split[0]
                else
                    split[1]
            Glide.with(itemView.context)
                .load("$BASE_ASSET_URL/Achievement/${data.badgeUrl}")
                .error(R.drawable.logo_color)
                .into(binding.imageProfileAchievement)
        }
    }
}