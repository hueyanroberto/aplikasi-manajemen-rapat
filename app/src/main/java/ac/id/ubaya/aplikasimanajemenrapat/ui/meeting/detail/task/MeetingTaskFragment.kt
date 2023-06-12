package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Task
import ac.id.ubaya.aplikasimanajemenrapat.databinding.FragmentMeetingTaskBinding
import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeetingTaskFragment(
    private val meeting: Meeting,
    private val token: String
) : Fragment() {

    private var _binding: FragmentMeetingTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMeetingTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getListTask(token, meeting.id)
        observeListToken()

//        binding.swipeRefreshTask.setOnRefreshListener {
//            viewModel.getListTask(token, meeting.id)
//        }

        binding.recyclerTask.layoutManager = LinearLayoutManager(context)
    }

    private fun observeListToken() {
        viewModel.task.observe(viewLifecycleOwner) { taskResponse ->
            when (taskResponse) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.progressBarTask.visibility = View.GONE
                    taskResponse.data?.let {
                        val adapter = TaskAdapter(it)
                        adapter.setOnItemClickCallback(object : TaskAdapter.OnItemClickCallback{
                            override fun onItemClickCallback(task: Task) {
                                val intent = Intent(activity, TaskDetailActivity::class.java)
                                intent.putExtra(TaskDetailActivity.EXTRA_TASK, task)
                                intent.putExtra(TaskDetailActivity.EXTRA_TOKEN, token)
                                intent.putExtra(TaskDetailActivity.EXTRA_ROLE, meeting.userRole)
                                activity?.startActivity(intent)
                            }
                        })
                        binding.recyclerTask.adapter = adapter
                        binding.viewEmpty.root.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                    }
                }
                is Resource.Error -> {
                    binding.progressBarTask.visibility = View.GONE
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
        viewModel.getListTask(token, meeting.id)
    }
}