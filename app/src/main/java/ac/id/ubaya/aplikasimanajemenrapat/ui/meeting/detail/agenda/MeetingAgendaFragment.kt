package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.agenda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Agenda
import ac.id.ubaya.aplikasimanajemenrapat.databinding.FragmentMeetingAgendaBinding
import androidx.recyclerview.widget.LinearLayoutManager

class MeetingAgendaFragment (
    private val agendas: List<Agenda>,
    private val token: String
) : Fragment() {

    private var _binding: FragmentMeetingAgendaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingAgendaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.recyclerMeetingAgenda) {
            layoutManager = LinearLayoutManager(context)
            val adapter = MeetingAgendaAdapter(agendas, token)
            this.adapter = adapter
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