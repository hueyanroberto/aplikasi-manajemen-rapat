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
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        binding.fabLeaderboardHistory.hide()
    }

    private fun getLeaderboard() {
        viewModel.getLeaderboard(token, organizationId).observe(viewLifecycleOwner) { leaderboardResource ->
            when (leaderboardResource) {
                is Resource.Loading -> {
                    binding.refreshLeaderboard.isRefreshing = true
                }
                is Resource.Success -> {
                    binding.refreshLeaderboard.isRefreshing = false

                    val leaderboard = leaderboardResource.data
                    leaderboard?.let {
                        binding.textLeaderboardPeriod.text = getString(R.string.leaderboard_period_date, convertDateFormatWithoutTime(it.startDate), convertDateFormatWithoutTime(it.endDate))
                        val adapter = LeaderboardAdapter(it.leaderboards)
                        binding.recyclerLeaderboard.layoutManager = LinearLayoutManager(context)
                        binding.recyclerLeaderboard.adapter = adapter

                        setupFab(it.period)
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

    @SuppressLint("CommitTransaction")
    private fun setupFab(period: Int) {
        binding.fabLeaderboardHistory.show()

        binding.recyclerLeaderboard.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && binding.fabLeaderboardHistory.isShown) {
                    binding.fabLeaderboardHistory.hide()
                } else if (dy < 0 && !binding.fabLeaderboardHistory.isShown) {
                    binding.fabLeaderboardHistory.show()
                }
            }
        })

        binding.fabLeaderboardHistory.setOnClickListener {
            if (period > 1) {
                val historyFragment = LeaderboardHistoryFragment.newInstance(organizationId, period, token)
                activity?.supportFragmentManager?.beginTransaction()?.let {
                    historyFragment.show(it, getString(R.string.leaderboard_history))
                }
            } else {
                Toast.makeText(context, getString(R.string.no_history), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}