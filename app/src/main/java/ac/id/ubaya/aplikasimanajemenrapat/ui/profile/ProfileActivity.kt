package ac.id.ubaya.aplikasimanajemenrapat.ui.profile

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityProfileBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.UserViewModel
import ac.id.ubaya.aplikasimanajemenrapat.ui.login.LoginActivity
import ac.id.ubaya.aplikasimanajemenrapat.ui.profile.achievement.AchievementActivity
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageProfileBack.setOnClickListener { finish() }

        init()
    }

    private fun init() {
        userViewModel.isUserGet.observe(this) { isUserGet->
            if (isUserGet) {
                binding.textProfileName.text = user.name
                binding.textProfileEmail.text = user.email
                getProfile()

                binding.textAchievementMore.setOnClickListener {
                    val intent = Intent(this, AchievementActivity::class.java)
                    intent.putExtra(AchievementActivity.EXTRA_TOKEN, user.token)
                    startActivity(intent)
                }
            }
        }

        userViewModel.getUser().observe(this) {
            if (it.id != -1) {
                user = it
                Log.d("MainActivity", user.toString())
                userViewModel.changeGetUserStatus()
            } else {
                Toast.makeText(this, resources.getString(R.string.user_not_login_error), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
        }
    }

    private fun getProfile() {
        lifecycleScope.launch {
            profileViewModel.getProfile(user.token.toString()).collect { userResource ->
                when (userResource) {
                    is Resource.Loading -> { }
                    is Resource.Success -> {
                        userResource.data?.let {
                            val textExp = "${it.exp}/${it.level?.maxExp}"
                            val progress: Float = ((it.exp.toFloat() - it.level!!.minExp.toFloat()) / (it.level!!.maxExp.toFloat() - it.level!!.minExp.toFloat())) * 100
                            Log.d("progress", progress.toString())
                            binding.textProfileName.text = it.name
                            binding.textProfileEmail.text = it.email
                            binding.textProfileLevel.text = it.level?.name
                            binding.textProfileExp.text = textExp
                            binding.progressBarLevel.progress = progress.toInt()

                            Glide.with(this@ProfileActivity)
                                .load("$BASE_ASSET_URL/Asset/Profile/User/${it.profilePic}")
                                .error(R.drawable.blank_profile)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(binding.imageProfile)

                            binding.recyclerProfileAchievement.layoutManager = LinearLayoutManager(this@ProfileActivity)
                            val adapter = ProfileAchievementAdapter(it.achievement)
                            binding.recyclerProfileAchievement.adapter = adapter
                        }
                    }
                    is Resource.Error -> {
                        Snackbar.make(binding.root, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .setAction(resources.getString(R.string.refresh)) {
                                getProfile()
                            }
                            .show()
                    }
                }
            }
        }
    }
}