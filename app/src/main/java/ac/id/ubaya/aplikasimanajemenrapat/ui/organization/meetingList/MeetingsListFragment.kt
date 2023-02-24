package ac.id.ubaya.aplikasimanajemenrapat.ui.organization.meetingList

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.id.ubaya.aplikasimanajemenrapat.databinding.FragmentMeetingsListBinding
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeetingsListFragment : Fragment() {

    private var _binding: FragmentMeetingsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MeetingListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = arguments?.getString("token", "")
        val organizationId = arguments?.getInt("organizationId", -1)

        if (token == null || organizationId == null ) return
        if (token.isEmpty() || organizationId == -1) return

        binding.recyclerMeetingList.layoutManager = LinearLayoutManager(context)

        getMeetingList(token, organizationId)
    }

    private fun getMeetingList(token: String, organizationId: Int) {
        viewModel.getListMeeting(token, organizationId).observe(viewLifecycleOwner) { meetingResource ->
            when (meetingResource) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.recyclerMeetingList.adapter = meetingResource.data?.let {
                        MeetingAdapter(it)
                    }
                }
                is Resource.Error -> {
                    Snackbar.make(binding.recyclerMeetingList, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.secondary_dark, context?.theme))
                        .setTextColor(resources.getColor(R.color.white, context?.theme))
                        .setAction(resources.getString(R.string.refresh)) {
                            getMeetingList(token, organizationId)
                        }
                        .show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}