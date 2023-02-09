package ac.id.ubaya.aplikasimanajemenrapat.ui.main

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityMainBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.login.LoginActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogout.setOnClickListener {
            mainViewModel.logOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }

        supportActionBar?.title = resources.getString(R.string.app_name)
    }
}