package ac.id.ubaya.aplikasimanajemenrapat.ui.profile.achievement

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityAchievementBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.profile.ProfileAchievementAdapter
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AchievementActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }

    private lateinit var binding: ActivityAchievementBinding
    private val viewModel: AchievementViewModel by viewModels()

    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        token = intent.getStringExtra(EXTRA_TOKEN).toString()
        getAchievements()

        binding.swipeAchievement.setOnRefreshListener { getAchievements() }
    }

    private fun getAchievements() {
        lifecycleScope.launch {
            viewModel.getAchievement(token).collect { achievementResource ->
                when (achievementResource) {
                    is Resource.Loading -> {
                        binding.swipeAchievement.isRefreshing = true
                    }
                    is Resource.Success -> {
                        binding.swipeAchievement.isRefreshing = false
                        achievementResource.data?.let {
                            val adapter = AchievementAdapter(it)
                            binding.recyclerAchievement.layoutManager = LinearLayoutManager(this@AchievementActivity)
                            binding.recyclerAchievement.adapter = adapter
                        }
                    }
                    is Resource.Error -> {
                        binding.swipeAchievement.isRefreshing = false
                        Snackbar.make(binding.recyclerAchievement, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .setAction(resources.getString(R.string.refresh)) {
                                getAchievements()
                            }
                            .show()
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}