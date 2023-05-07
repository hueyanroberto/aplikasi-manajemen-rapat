package ac.id.ubaya.aplikasimanajemenrapat.ui.profile

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityProfileBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OtherProfileActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
        const val EXTRA_TOKEN = "extra_token"
    }

    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.textAchievementMore.visibility = View.GONE
        binding.textProfileTitle.text = ""

        val userId = intent.getIntExtra(EXTRA_USER_ID, -1)
        val token = intent.getStringExtra(EXTRA_TOKEN).toString()
        getProfile(token, userId)
    }

    private fun getProfile(token: String, userId: Int) {
        lifecycleScope.launch {
            profileViewModel.getOtherProfile(token, userId).collect { userResource ->
                when (userResource) {
                    is Resource.Loading -> { }
                    is Resource.Success -> {
                        userResource.data?.let {
                            Log.d("UserData", it.toString())

                            val textExp = "${it.exp}/${it.level?.maxExp}"
                            val progress: Float = ((it.exp.toFloat() - it.level!!.minExp.toFloat()) / (it.level!!.maxExp.toFloat() - it.level!!.minExp.toFloat())) * 100
                            Log.d("progress", progress.toString())
                            binding.textProfileTitle.text = it.name
                            binding.textProfileName.text = it.name
                            binding.textProfileEmail.text = it.email
                            binding.textProfileLevel.text = it.level?.name
                            binding.textProfileExp.text = textExp
                            binding.progressBarLevel.progress = progress.toInt()

                            when (it.levelId) {
                                1 -> {
                                    binding.textProfileLevel.setBackgroundResource(R.drawable.level_bg_bronze)
                                    binding.imageBadge.setBackgroundResource(R.drawable.badges_02)
                                }
                                2 -> {
                                    binding.textProfileLevel.setBackgroundResource(R.drawable.level_bg_silver)
                                    binding.imageBadge.setBackgroundResource(R.drawable.badges_03)
                                }
                                3 -> {
                                    binding.textProfileLevel.setBackgroundResource(R.drawable.level_bg_gold)
                                    binding.imageBadge.setBackgroundResource(R.drawable.badges_04)
                                }
                                4 -> {
                                    binding.textProfileLevel.setBackgroundResource(R.drawable.level_bg_emerald)
                                    binding.imageBadge.setBackgroundResource(R.drawable.badges_05)
                                }
                                5 -> {
                                    binding.textProfileLevel.setBackgroundResource(R.drawable.level_bg_ruby)
                                    binding.imageBadge.setBackgroundResource(R.drawable.badges_06)
                                }
                                6 -> {
                                    binding.textProfileLevel.setBackgroundResource(R.drawable.level_bg_diamond)
                                    binding.imageBadge.setBackgroundResource(R.drawable.badges_07)
                                }
                            }

                            Glide.with(this@OtherProfileActivity)
                                .load("$BASE_ASSET_URL/Profile/User/${it.profilePic}")
                                .error(R.drawable.blank_profile)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(binding.imageProfile)

                            binding.recyclerProfileAchievement.layoutManager = LinearLayoutManager(this@OtherProfileActivity)
                            val adapter = ProfileAchievementAdapter(it.achievement)
                            binding.recyclerProfileAchievement.adapter = adapter

                            binding.viewEmpty.root.visibility = if (it.achievement.isEmpty()) View.VISIBLE else View.GONE
                        }
                    }
                    is Resource.Error -> {
                        Snackbar.make(binding.root, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .setAction(resources.getString(R.string.refresh)) {
                                getProfile(token, userId)
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