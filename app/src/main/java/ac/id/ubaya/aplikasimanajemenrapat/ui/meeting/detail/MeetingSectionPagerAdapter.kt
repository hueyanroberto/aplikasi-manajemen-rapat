package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail

import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda.MeetingAgendaFragment
import ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.participant.MeetingParticipantFragment
import ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.task.MeetingTaskFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MeetingSectionPagerAdapter(
    activity: AppCompatActivity,
    private val meeting: Meeting,
    private val token: String
): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MeetingDetailFragment(meeting, token)
            1 -> fragment = MeetingAgendaFragment(meeting, token)
            2 -> fragment = MeetingParticipantFragment(meeting.participant)
            3 -> fragment = MeetingTaskFragment()
        }
        return  fragment as Fragment
    }
}