package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.create

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemSelectParticipantBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ChooseParticipantAdapter(
    private val listUser: List<User>,
    private val listParticipant: ArrayList<Int>
): RecyclerView.Adapter<ChooseParticipantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_participant, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listUser[position]
        holder.bind(data, listParticipant)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemSelectParticipantBinding.bind(itemView)
        fun bind (data: User, listParticipant: ArrayList<Int>) {
            binding.textSelectParticipantName.text = data.name
            binding.textSelectParticipantRole.text = data.role?.name
            Glide.with(itemView.context)
                .load("$BASE_ASSET_URL/Profile/user/${data.profilePic}")
                .error(R.drawable.blank_profile)
                .into(binding.imageSelectParticipant)

            var isChecked = false
            for (participant in listParticipant) {
                if (data.id == participant) {
                    isChecked = true
                    break
                }
            }
            binding.checkSelectParticipant.isChecked = isChecked

            binding.checkSelectParticipant.setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    listParticipant.add(data.id)
                } else {
                    for (participant in listParticipant) {
                        if (data.id == participant) {
                            listParticipant.remove(participant)
                            break
                        }
                    }
                }
                Log.d("ChooseParticipant", listParticipant.toString())
            }

            itemView.setOnClickListener {
                binding.checkSelectParticipant.isChecked = !binding.checkSelectParticipant.isChecked
            }
        }
    }
}