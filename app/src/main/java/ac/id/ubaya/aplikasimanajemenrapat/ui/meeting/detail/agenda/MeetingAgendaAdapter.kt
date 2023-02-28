package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Agenda
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemAgendaBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MeetingAgendaAdapter(private val listAgenda: List<Agenda>): RecyclerView.Adapter<MeetingAgendaAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemAgendaBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAgendaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listAgenda.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listAgenda[position]
        with(holder) {
            binding.textAgendaTask.text = itemView.context.getString(R.string.agenda_task, (position + 1).toString(), data.task)

            itemView.setOnClickListener {

            }
        }
    }
}