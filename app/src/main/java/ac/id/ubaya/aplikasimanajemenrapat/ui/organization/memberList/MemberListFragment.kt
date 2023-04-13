package ac.id.ubaya.aplikasimanajemenrapat.ui.organization.memberList

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.id.ubaya.aplikasimanajemenrapat.databinding.FragmentMemberListBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.profile.OtherProfileActivity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MemberListFragment : Fragment() {

    private var _binding: FragmentMemberListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MemberListViewModel by viewModels()
    private lateinit var adapter: MemberListAdapter

    var token = ""
    var organizationId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            Log.d("memberFragment", it.toString())
            val token = it.getString("token", "").toString()
            val organizationId = it.getInt("organizationId", -1)
            val roleId = it.getInt("roleId", -1)

            if (token.isEmpty() || organizationId == -1 || roleId == -1) return

            binding.recyclerMemberList.layoutManager = LinearLayoutManager(context)

            getOrganizationMember(token, organizationId, roleId)
        }
    }

    private fun getOrganizationMember(token: String, organizationId: Int, roleId: Int) {
        viewModel.getOrganizationMember(token, organizationId).observe(viewLifecycleOwner) { userResource ->
            when (userResource) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (userResource.data != null) {
                        adapter = MemberListAdapter(userResource.data)
                        adapter.setOnItemClickCallback(object : MemberListAdapter.OnItemClickCallBack {
                            override fun onItemLongClickCallback(user: User, position: Int) {
                                showAlertDialogChooser(user, position, token, organizationId, roleId)
                            }

                            override fun onItemClickCallback(user: User) {
                                val intent = Intent(context, OtherProfileActivity::class.java)
                                intent.putExtra(OtherProfileActivity.EXTRA_USER_ID, user.id)
                                intent.putExtra(OtherProfileActivity.EXTRA_TOKEN, token)
                                activity?.startActivity(intent)
                            }
                        })
                        binding.recyclerMemberList.adapter = adapter
                        binding.viewEmpty.root.visibility = if (userResource.data.isEmpty()) View.VISIBLE else View.GONE
                    }
                }
                is Resource.Error -> {
                    Snackbar.make(binding.recyclerMemberList, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.secondary_dark, context?.theme))
                        .setTextColor(resources.getColor(R.color.white, context?.theme))
                        .setAction(resources.getString(R.string.refresh)) {
                            getOrganizationMember(token, organizationId,roleId)
                        }
                        .show()
                }
            }
        }
    }

    private fun showAlertDialogChooser(data: User, position: Int, token: String, organizationId: Int, roleId: Int) {
        var option = arrayOf(resources.getString(R.string.send_email))
        if (roleId == 1) {
            if (data.role?.id == 3) {
                option = arrayOf(resources.getString(R.string.send_email), resources.getString(R.string.set_as_admin))
            } else if (data.role?.id == 2){
                option = arrayOf(resources.getString(R.string.send_email), resources.getString(R.string.set_as_member))
            }
        }
        AlertDialog.Builder(context)
            .setItems(option) {_, pos ->
                when (pos) {
                    0 -> {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            this.data = Uri.parse("mailto:")
                            putExtra(Intent.EXTRA_EMAIL, arrayOf(data.email))
                        }
                        context?.startActivity(intent)
                    }
                    1 -> {
                        when (data.role?.id) {
                            2 -> updateRole(data, position, token, organizationId, data.id, 3)
                            3 -> updateRole(data, position, token, organizationId, data.id, 2)
                        }
                    }
                }
            }
            .show()
    }

    private fun updateRole(data: User, position: Int, token: String, organizationId: Int, userId: Int, roleId: Int) {
        lifecycleScope.launch {
            viewModel.updateRole(token, organizationId, userId, roleId).collect { userResource ->
                when (userResource) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        val user = userResource.data
                        data.role = user?.role
                        adapter.notifyItemChanged(position)
                    }
                    is Resource.Error -> {
                        Snackbar.make(binding.recyclerMemberList, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, context?.theme))
                            .setTextColor(resources.getColor(R.color.white, context?.theme))
                            .setAction(resources.getString(R.string.refresh)) {
                                updateRole(data, position, token, organizationId, userId, roleId)
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
}