package ac.id.ubaya.aplikasimanajemenrapat.ui.organization.meetingList

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.id.ubaya.aplikasimanajemenrapat.databinding.FragmentMeetingsListBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.create.CreateMeetingActivity
import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeetingsListFragment : Fragment() {

    private var _binding: FragmentMeetingsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MeetingListViewModel by viewModels()

    private var token = ""
    private var organizationId = -1
    private var roleId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = requireArguments().getString("token", "")
        organizationId = requireArguments().getInt("organizationId", -1)
        roleId = requireArguments().getInt("roleId", -1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabCreateMeeting.hide()

        if (token.isEmpty() || organizationId == -1) return

        binding.recyclerMeetingList.layoutManager = LinearLayoutManager(context)

        getMeetingList(token, organizationId)

        if (roleId != 3) {
            binding.fabCreateMeeting.show()

            binding.fabCreateMeeting.setOnClickListener {
                val intent = Intent(context, CreateMeetingActivity::class.java)
                intent.putExtra(CreateMeetingActivity.EXTRA_ORGANIZATION_ID, organizationId)
                context?.startActivity(intent)
            }
        }

        binding.swipeRefreshMeeting.setOnRefreshListener {
            getMeetingList(token, organizationId)
        }
    }

    private fun getMeetingList(token: String, organizationId: Int) {
        viewModel.getListMeeting(token, organizationId).observe(viewLifecycleOwner) { meetingResource ->
            when (meetingResource) {
                is Resource.Loading -> {
                    binding.swipeRefreshMeeting.isRefreshing = true
                }
                is Resource.Success -> {
                    binding.swipeRefreshMeeting.isRefreshing = false
                    binding.recyclerMeetingList.adapter = meetingResource.data?.let {
                        MeetingAdapter(it)
                    }
                }
                is Resource.Error -> {
                    binding.swipeRefreshMeeting.isRefreshing = false
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

    override fun onResume() {
        super.onResume()
        if (newMeetingAdded) {
            newMeetingAdded = false
            getMeetingList(token, organizationId)
        }

    }

    companion object {
        var newMeetingAdded = false
    }
}