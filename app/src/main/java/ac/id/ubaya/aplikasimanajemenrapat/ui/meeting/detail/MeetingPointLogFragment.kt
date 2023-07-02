package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.FragmentMeetingPointLogBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val ARG_TOKEN = "arg_token"
private const val ARG_MEETING_ID = "arg_meeting_id"

@AndroidEntryPoint
class MeetingPointLogFragment : DialogFragment() {
    private var token = ""
    private var meetingId = -1

    private var _binding: FragmentMeetingPointLogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MeetingPointViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString(ARG_TOKEN).toString()
            meetingId = it.getInt(ARG_MEETING_ID, -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingPointLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonClose.setOnClickListener { dismiss() }
        binding.recyclerMeetingPoint.layoutManager = LinearLayoutManager(context)
        getMeetingPointLog(token, meetingId)
    }

    private fun getMeetingPointLog(token: String, meetingId: Int) {
        lifecycleScope.launch {
            viewModel.getMeetingPointLog(token, meetingId).collect { meetingPointResource ->
                when (meetingPointResource) {
                    is Resource.Loading -> {
                        binding.progressBarMeetingPoint.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarMeetingPoint.visibility = View.GONE
                        val meetingPoints = meetingPointResource.data
                        meetingPoints?.let {
                            val adapter = MeetingPointAdapter(it)
                            binding.recyclerMeetingPoint.adapter = adapter
                            binding.viewEmpty.root.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBarMeetingPoint.visibility = View.GONE
                        Snackbar.make(binding.recyclerMeetingPoint, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, context?.theme))
                            .setTextColor(resources.getColor(R.color.white, context?.theme))
                            .setAction(resources.getString(R.string.refresh)) {
                                getMeetingPointLog(token, meetingId)
                            }
                            .show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(token: String, meetingId: Int) =
            MeetingPointLogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TOKEN, token)
                    putInt(ARG_MEETING_ID, meetingId)
                }
            }
    }
}