package ac.id.ubaya.aplikasimanajemenrapat.ui.organization.memberList

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.id.ubaya.aplikasimanajemenrapat.databinding.FragmentMemberListBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.organization.meetingList.MeetingAdapter
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberListFragment : Fragment() {

    private var _binding: FragmentMemberListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MemberListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = arguments?.getString("token", "")
        val organizationId = arguments?.getInt("organizationId", -1)

        if (token == null || organizationId == null ) return
        if (token.isEmpty() || organizationId == -1) return

        binding.recyclerMemberList.layoutManager = LinearLayoutManager(context)

        getOrganizationMember(token, organizationId)
    }

    private fun getOrganizationMember(token: String, organizationId: Int) {
        viewModel.getOrganizationMember(token, organizationId).observe(viewLifecycleOwner) { userResource ->
            when (userResource) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.recyclerMemberList.adapter = userResource.data?.let {
                        MemberListAdapter(it)
                    }
                }
                is Resource.Error -> {
                    Snackbar.make(binding.recyclerMemberList, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.secondary_dark, context?.theme))
                        .setTextColor(resources.getColor(R.color.white, context?.theme))
                        .setAction(resources.getString(R.string.refresh)) {
                            getOrganizationMember(token, organizationId)
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