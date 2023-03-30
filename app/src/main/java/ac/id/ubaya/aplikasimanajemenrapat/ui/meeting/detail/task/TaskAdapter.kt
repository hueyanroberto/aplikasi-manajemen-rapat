package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.task

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Task
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemTaskBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val listTask: List<Task>): RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listTask.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listTask[position]
        with(holder) {
            binding.textTaskTitle.text = data.title
            binding.textTaskAssign.text = itemView.context.getString(R.string.assign_to_someone, data.user)
            binding.textTaskDeadline.text = convertDateFormat(data.deadline)
            when (data.status) {
                0 -> {
                    binding.constraintCardTask.setBackgroundColor(itemView.context.getColor(R.color.primary_light))
                    binding.textTaskStatus.setBackgroundResource(R.drawable.rounded_pill_primary)
                    binding.textTaskStatus.text = itemView.context.getText(R.string.not_completed)
                }
                1 -> {
                    binding.constraintCardTask.setBackgroundColor(itemView.context.getColor(R.color.light_gray))
                    binding.textTaskStatus.setBackgroundResource(R.drawable.rounded_pill_grey)
                    binding.textTaskStatus.text = itemView.context.getText(R.string.completed)
                }
            }

            itemView.setOnClickListener {

            }
        }
    }
}