package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.MeetingPoint
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemMeetingPointBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
import java.util.Locale

class MeetingPointAdapter(private val meetingPoints: List<MeetingPoint>): RecyclerView.Adapter<MeetingPointAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemMeetingPointBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMeetingPointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = meetingPoints.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = meetingPoints[position]
        with(holder) {
            val desc = data.description
            val split = desc.split("||")
            try {
                binding.textMeetingPointDescription.text =
                    if (Locale.getDefault().language == "id") split[0]
                    else split[1]
            } catch (e: Exception) {
                binding.textMeetingPoint.text = data.description
            }

            binding.textMeetingPoint.text = if (data.point < 0) {
                binding.textMeetingPoint.setTextColor(itemView.context.getColor(R.color.secondary_dark))
                data.point.toString()
            } else if (data.point > 0) {
                binding.textMeetingPoint.setTextColor(itemView.context.getColor(R.color.primary_dark))
                "+" + data.point.toString()
            } else {
                binding.textMeetingPoint.setTextColor(itemView.context.getColor(R.color.black))
                "0"
            }
        }
    }
}