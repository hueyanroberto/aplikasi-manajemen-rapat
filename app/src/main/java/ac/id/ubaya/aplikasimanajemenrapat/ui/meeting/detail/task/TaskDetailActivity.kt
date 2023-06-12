package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.task

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Task
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityTaskDetailBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class TaskDetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_TASK = "extra_task"
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_ROLE = "extra_role"
    }

    private lateinit var binding: ActivityTaskDetailBinding
    private val viewModel: TaskDetailViewModel by viewModels()
    private var task: Task? = null

    private lateinit var token: String
    private var userRole = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        token = intent.getStringExtra(EXTRA_TOKEN).toString()
        userRole = intent.getIntExtra(EXTRA_ROLE, -1)
        task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_TASK)
        }

        task?.let {
            Log.d("Task", it.toString())
            binding.textTaskTitle.text = it.title
            binding.textTaskDesc.text = it.description
            binding.textTaskDeadline.text = resources.getString(R.string.deadline_at, convertDateFormat(it.deadline))
            binding.textTaskAssignTo.text = resources.getString(R.string.assign_to_someone, it.user)
            if (it.status == 0)
                binding.textTaskStatus.text = resources.getString(R.string.suggestion_status, getString(R.string.not_completed))
            else
                binding.textTaskStatus.text = resources.getString(R.string.suggestion_status, getString(R.string.completed))

            if (userRole == 1 && it.status == 0) {
                binding.buttonMarkTaskAsDone.setOnClickListener(this)
                binding.buttonMarkTaskAsDone.visibility = View.VISIBLE
            } else {
                binding.buttonMarkTaskAsDone.visibility = View.GONE
            }

            binding.textInfo.visibility = if (userRole == 1) View.GONE else View.VISIBLE
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.buttonMarkTaskAsDone.id -> {
                AlertDialog.Builder(this)
                    .setMessage(getString(R.string.mark_task_done_message))
                    .setPositiveButton(getString(R.string.mark_as_done)) {_,_ ->
                        markTaskAsDone(token, task?.id!!)
                    }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show()
            }
        }
    }

    private fun markTaskAsDone(token: String, taskId: Int) {
        lifecycleScope.launch {
            viewModel.updateTaskStatus(token, taskId, Date()).collect { taskResource ->
                when (taskResource) {
                    is Resource.Loading -> {
                        binding.buttonMarkTaskAsDone.setBackgroundResource(R.drawable.button_disable)
                        binding.buttonMarkTaskAsDone.setOnClickListener(null)
                    }
                    is Resource.Success -> {
                        binding.buttonMarkTaskAsDone.visibility = View.GONE
                        binding.textTaskStatus.text = resources.getString(R.string.suggestion_status, getString(R.string.completed))
                        Toast.makeText(this@TaskDetailActivity, getString(R.string.task_done), Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error -> {
                        binding.buttonMarkTaskAsDone.setBackgroundResource(R.drawable.button_primary)
                        binding.buttonMarkTaskAsDone.setOnClickListener(this@TaskDetailActivity)
                        Snackbar.make(binding.buttonMarkTaskAsDone, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .show()
                    }
                }
            }
        }
    }
}