package ac.id.ubaya.aplikasimanajemenrapat.ui.organization.meetingList

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemMeetingBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MeetingAdapter (private val meetings: List<Meeting>): RecyclerView.Adapter<MeetingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meeting, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = meetings.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = meetings[position]
        holder.bind(data)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemMeetingBinding.bind(itemView)

        fun bind(data: Meeting) {
            val startTime = convertDateFormat(data.startTime)
            val endTime = convertDateFormat(data.endTime)
            val endSplit = endTime.split(" ")
            val time = "$startTime - ${endSplit[3]}"

            binding.textMeetingTitle.text = data.title
            binding.textMeetingTime.text = time

            when (data.status) {
                0 -> binding.textMeetingStatus.text = itemView.context.getText(R.string.meeting_not_started)
                1 -> binding.textMeetingStatus.text = itemView.context.getText(R.string.meeting_started)
                2 -> binding.textMeetingStatus.text = itemView.context.getText(R.string.meeting_ended)
            }
        }
    }
}