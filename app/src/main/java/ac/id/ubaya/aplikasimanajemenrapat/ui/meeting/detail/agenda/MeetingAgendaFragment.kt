package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Agenda
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.databinding.FragmentMeetingAgendaBinding
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager

class MeetingAgendaFragment (
    private val meeting: Meeting,
    private val token: String
) : Fragment() {

    private var _binding: FragmentMeetingAgendaBinding? = null
    private val binding get() = _binding!!

    private lateinit var agendas: List<Agenda>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        agendas = meeting.agenda
        _binding = FragmentMeetingAgendaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.recyclerMeetingAgenda) {
            layoutManager = LinearLayoutManager(context)
            val adapter = MeetingAgendaAdapter(agendas)
            adapter.setOnItemClickedCallback(object: MeetingAgendaAdapter.OnItemClickedCallback {
                override fun onItemClickedCallback(data: Agenda) {
                    val intent = Intent(context, DetailAgendaActivity::class.java)
                    intent.putExtra(DetailAgendaActivity.EXTRA_AGENDA, data)
                    intent.putExtra(DetailAgendaActivity.EXTRA_TOKEN, token)
                    intent.putExtra(DetailAgendaActivity.EXTRA_ROLE, meeting.userRole)
                    intent.putExtra(DetailAgendaActivity.EXTRA_MEETING_STATUS, meeting.status)
                    activity?.startActivity(intent)
                }
            })
            this.adapter = adapter
            binding.viewEmpty.root.visibility = if (agendas.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}