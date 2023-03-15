package ac.id.ubaya.aplikasimanajemenrapat.ui.profile.achievement

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Achievement
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemAchievementBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.*

class AchievementAdapter(
    private val listAchievement: List<Achievement>
): RecyclerView.Adapter<AchievementAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemAchievementBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listAchievement.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listAchievement[position]
        with(holder) {
            val name = data.name
            val split = name.split("||")
            val progressPercent = (data.progress.toFloat() / data.milestone) * 100
            val progress = "${data.progress}/${data.milestone}"

            binding.textAchievementName.text =
                if (Locale.getDefault().language == "id")
                    split[0]
                else
                    split[1]

            Glide.with(itemView.context)
                .load("$BASE_ASSET_URL/Asset/Achievement/${data.badgeUrl}")
                .error(R.drawable.logo_color)
                .into(binding.imageAchievement)

            binding.textAchievementReward.text = itemView.context.getString(R.string.reward, data.rewardExp.toString())
            binding.progressBarAchievement.progress = progressPercent.toInt()
            binding.textAchievementProgress.text = progress

            if (data.status == 0) {
                binding.constraintAchievement.setBackgroundResource(R.color.light_gray)

                val colorMatrix = ColorMatrix()
                colorMatrix.setSaturation(0f)
                val filter = ColorMatrixColorFilter(colorMatrix)
                binding.imageAchievement.colorFilter = filter
            } else {
                binding.constraintAchievement.setBackgroundResource(R.color.white)
                binding.imageAchievement.colorFilter = null
            }
        }
    }
}