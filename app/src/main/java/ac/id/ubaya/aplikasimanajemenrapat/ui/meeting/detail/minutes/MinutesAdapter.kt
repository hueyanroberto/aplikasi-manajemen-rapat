package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.minutes

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Agenda
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Suggestion
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemMinutesGroupBinding
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemMinutesListBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter

class MinutesAdapter (
    private val context: Context,
    private val agenda: List<Agenda>
): BaseExpandableListAdapter() {
    override fun getGroupCount(): Int = agenda.size

    override fun getChildrenCount(position: Int): Int {
        return if ((agenda[position].suggestions?.size ?: 0) > 0) {
            agenda[position].suggestions?.size!!
        } else {
            1
        }
    }

    override fun getGroup(position: Int): Any = agenda[position]

    override fun getChild(pPosition: Int, cPosition: Int): Suggestion? = agenda[pPosition].suggestions?.get(cPosition)

    override fun getGroupId(p0: Int): Long = p0.toLong()

    override fun getChildId(p0: Int, p1: Int): Long = p1.toLong()

    override fun hasStableIds(): Boolean = false

    override fun getGroupView(position: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        val data = agenda[position]

        val inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemMinutesGroupBinding.inflate(inflater)
        binding.textMinutesAgenda.text = this.context.getString(R.string.agenda_task, (position + 1).toString(), data.task)

        return binding.root
    }

    override fun getChildView(pPosition: Int, cPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {

        val inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemMinutesListBinding.inflate(inflater)

        try {
            val data = agenda[pPosition].suggestions?.get(cPosition)
            data?.let {
                binding.textMinutes.text = it.suggestion
                binding.textSuggestedBy.text = this.context.getString(R.string.suggested_by, it.user)
            }
        } catch (e: Exception) {
            binding.textSuggestedBy.visibility = View.GONE
            binding.textMinutes.text = this.context.getString(R.string.no_suggestion)
        }

        return binding.root
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean = false

}