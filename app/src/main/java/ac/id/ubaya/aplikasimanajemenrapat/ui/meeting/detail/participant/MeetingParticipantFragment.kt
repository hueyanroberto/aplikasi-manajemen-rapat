package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.participant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Participant
import ac.id.ubaya.aplikasimanajemenrapat.databinding.FragmentMeetingParticipantBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.profile.OtherProfileActivity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager

class MeetingParticipantFragment (
    private val participants: List<Participant>,
    private val token: String
) : Fragment() {

    private var _binding: FragmentMeetingParticipantBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingParticipantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerMeetingParticipant) {
            layoutManager = LinearLayoutManager(context)
            val adapter = MeetingParticipantAdapter(participants)
            adapter.setOnItemLongClickCallback(object : MeetingParticipantAdapter.OnItemClickCallback{
                override fun onItemClickCallback(participant: Participant) {
                    val intent = Intent(context, OtherProfileActivity::class.java)
                    intent.putExtra(OtherProfileActivity.EXTRA_USER_ID, participant.id)
                    intent.putExtra(OtherProfileActivity.EXTRA_TOKEN, token)
                    activity?.startActivity(intent)
                }
            })
            this.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}