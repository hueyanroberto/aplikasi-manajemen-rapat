package ac.id.ubaya.aplikasimanajemenrapat.ui.organization.leaderboard

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Leaderboard
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemLeaderboardBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertNumber
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class LeaderboardAdapter(
    private val listLeaderboard: List<Leaderboard>
): RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemLeaderboardBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemLeaderboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder((view))
    }

    override fun getItemCount(): Int = listLeaderboard.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listLeaderboard[position]
        with(holder) {
            binding.textLeaderboardName.text = data.name
            binding.textLeaderboardPoint.text = itemView.context.getString(R.string.points, data.pointsGet.toString())
            binding.textLeaderboardPosition.text = convertNumber(position + 1)

            Glide.with(itemView.context)
                .load("$BASE_ASSET_URL/Profile/User/${data.profilePic}")
                .error(R.drawable.blank_profile)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.imageLeaderboard)
        }
    }
}