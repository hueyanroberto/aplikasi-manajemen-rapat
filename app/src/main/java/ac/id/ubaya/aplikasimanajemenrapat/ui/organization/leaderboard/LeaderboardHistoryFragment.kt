package ac.id.ubaya.aplikasimanajemenrapat.ui.organization.leaderboard

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.id.ubaya.aplikasimanajemenrapat.databinding.FragmentLeaderboardHistoryBinding
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val ARG_ORGANIZATION_ID = "arg_organization_id"
private const val ARG_PERIOD = "arg_period"
private const val ARG_TOKEN = "arg_token"

@AndroidEntryPoint
class LeaderboardHistoryFragment : DialogFragment(), View.OnClickListener {
    private var organizationId: Int = -1
    private var period: Int = 0
    private var currentPeriod = 0
    private var token = ""

    private var _binding: FragmentLeaderboardHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LeaderboardHistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            organizationId = it.getInt(ARG_ORGANIZATION_ID, -1)
            period = it.getInt(ARG_PERIOD, 0)
            token = it.getString(ARG_TOKEN, "")
            currentPeriod = period - 1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = FragmentLeaderboardHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textHistoryPeriod.text = getString(R.string.history_period, currentPeriod.toString())

        binding.imageHistoryNext.setOnClickListener(this)
        binding.imageHistoryPrev.setOnClickListener(this)

        getHistory(organizationId, currentPeriod)

        binding.swipeRefreshLeaderboardHistory.setOnRefreshListener {
            getHistory(organizationId, currentPeriod)
        }
    }

    private fun getHistory(organizationId: Int, period: Int) {
        lifecycleScope.launch {
            viewModel.getLeaderboardHistory(token, organizationId, period).collect { leaderboardResource ->
                when (leaderboardResource) {
                    is Resource.Loading -> {
                        binding.swipeRefreshLeaderboardHistory.isRefreshing = true
                    }
                    is Resource.Success -> {
                        binding.swipeRefreshLeaderboardHistory.isRefreshing = false

                        val leaderboard = leaderboardResource.data
                        leaderboard?.let {
                            val adapter = LeaderboardAdapter(it.leaderboards)
                            binding.recyclerLeaderboardHistory.layoutManager = LinearLayoutManager(context)
                            binding.recyclerLeaderboardHistory.adapter = adapter
                        }
                    }
                    is Resource.Error -> {
                        binding.swipeRefreshLeaderboardHistory.isRefreshing = false
                        Snackbar.make(binding.recyclerLeaderboardHistory, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, context?.theme))
                            .setTextColor(resources.getColor(R.color.white, context?.theme))
                            .setAction(resources.getString(R.string.refresh)) {
                                getHistory(organizationId, period)
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

    override fun onClick(v: View) {
        when (v.id) {
            binding.imageHistoryNext.id -> {
                if (currentPeriod + 1 >= period) {
                    Toast.makeText(context, getString(R.string.no_next_history), Toast.LENGTH_SHORT).show()
                } else {
                    currentPeriod++
                    getHistory(organizationId, currentPeriod)
                    binding.textHistoryPeriod.text = getString(R.string.history_period, currentPeriod.toString())
                }
            }
            binding.imageHistoryPrev.id -> {
                if (currentPeriod - 1 <= 0) {
                    Toast.makeText(context, getString(R.string.no_prev_history), Toast.LENGTH_SHORT).show()
                } else {
                    currentPeriod--
                    getHistory(organizationId, currentPeriod)
                    binding.textHistoryPeriod.text = getString(R.string.history_period, currentPeriod.toString())
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(organizationId: Int, period: Int, token: String) =
            LeaderboardHistoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ORGANIZATION_ID, organizationId)
                    putInt(ARG_PERIOD, period)
                    putString(ARG_TOKEN, token)
                }
            }
    }
}