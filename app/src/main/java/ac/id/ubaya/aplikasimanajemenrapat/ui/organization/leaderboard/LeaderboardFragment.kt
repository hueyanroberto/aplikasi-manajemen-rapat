package ac.id.ubaya.aplikasimanajemenrapat.ui.organization.leaderboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.FragmentLeaderboardBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertDateFormatWithoutTime
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaderboardFragment : Fragment() {

    private var _binding: FragmentLeaderboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LeaderboardViewModel by viewModels()

    var token = ""
    var organizationId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = requireArguments().getString("token").toString()
        organizationId = requireArguments().getInt("organizationId")

        getLeaderboard()
        binding.refreshLeaderboard.setOnRefreshListener { getLeaderboard() }
    }

    private fun getLeaderboard() {
        viewModel.getLeaderboard(token, organizationId).observe(viewLifecycleOwner) { leaderboardResponse ->
            when (leaderboardResponse) {
                is Resource.Loading -> {
                    binding.refreshLeaderboard.isRefreshing = true
                }
                is Resource.Success -> {
                    binding.refreshLeaderboard.isRefreshing = false

                    val leaderboard = leaderboardResponse.data
                    leaderboard?.let {
                        binding.textLeaderboardPeriod.text = getString(R.string.leaderboard_period_date, convertDateFormatWithoutTime(it.startDate), convertDateFormatWithoutTime(it.endDate))
                        val adapter = LeaderboardAdapter(it.leaderboards)
                        binding.recyclerLeaderboard.layoutManager = LinearLayoutManager(context)
                        binding.recyclerLeaderboard.adapter = adapter
                    }
                }
                is Resource.Error -> {
                    binding.refreshLeaderboard.isRefreshing = false
                    Snackbar.make(binding.recyclerLeaderboard, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.secondary_dark, context?.theme))
                        .setTextColor(resources.getColor(R.color.white, context?.theme))
                        .setAction(resources.getString(R.string.refresh)) {
                            getLeaderboard()
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