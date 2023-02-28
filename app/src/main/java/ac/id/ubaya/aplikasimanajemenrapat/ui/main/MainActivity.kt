package ac.id.ubaya.aplikasimanajemenrapat.ui.main

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityMainBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.login.LoginActivity
import ac.id.ubaya.aplikasimanajemenrapat.ui.organization.CreateOrganizationActivity
import ac.id.ubaya.aplikasimanajemenrapat.ui.organization.JoinOrganizationActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()
    private var isAllFabVisible = false
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpFab()

        binding.recyclerOrganizations.layoutManager = LinearLayoutManager(this)

        binding.refreshMain.setOnRefreshListener {
            binding.refreshMain.isRefreshing = false
        }

        init()
    }

    private fun init() {
        mainViewModel.isUserGet.observe(this) { isUserGet ->
            if (isUserGet) {
                getListOrganization()

                binding.refreshMain.setOnRefreshListener {
                    getListOrganization()
                }

                binding.imageLogOut.setOnClickListener {
                    mainViewModel.logOut(user?.token.toString())
                }
            }
        }

        mainViewModel.getUser().observe(this) {
            if (it.id != -1) {
                user = it
                Log.d("MainActivity", user.toString())
                mainViewModel.changeGetUserStatus()
            } else {
                Toast.makeText(this, resources.getString(R.string.user_not_login_error), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
        }
    }

    private fun getListOrganization() {
        Log.d("MainActivity", "getListOrganization: Method dipanggil")
        user?.let {
            mainViewModel.getListOrganization(it.token.toString()).observe(this) { organizationResponse ->
                when (organizationResponse) {
                    is Resource.Loading -> {
                        binding.progressBarMain.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarMain.visibility = View.GONE
                        binding.refreshMain.isRefreshing = false

                        val organization = organizationResponse.data
                        if (organization != null) {
                            val adapter = OrganizationAdapter(organization)
                            binding.recyclerOrganizations.adapter = adapter
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBarMain.visibility = View.GONE
                        binding.refreshMain.isRefreshing = false

                        val organization = organizationResponse.data
                        if (organization != null) {
                            val adapter = OrganizationAdapter(organization)
                            binding.recyclerOrganizations.adapter = adapter
                        }
                        Snackbar.make(binding.fabAddOrganization, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .setAction(resources.getString(R.string.refresh)) {
                                getListOrganization()
                            }
                            .show()
                    }
                }
            }
        }
    }

    private fun setUpFab() {
        binding.relativeMainOverlay.visibility = View.GONE
        binding.fabCreateOrganization.visibility = View.GONE
        binding.fabJoinOrganization.visibility = View.GONE
        binding.textCreateOrganization.visibility = View.GONE
        binding.textJoinOrganization.visibility = View.GONE

        binding.fabAddOrganization.shrink()

        binding.fabAddOrganization.setOnClickListener {
            showFab(!isAllFabVisible)
        }

        binding.relativeMainOverlay.setOnClickListener {
            showFab(false)
        }

        binding.fabCreateOrganization.setOnClickListener {
            showFab(false)
            startActivity(Intent(this, CreateOrganizationActivity::class.java))
        }

        binding.fabJoinOrganization.setOnClickListener {
            showFab(false)
            startActivity(Intent(this, JoinOrganizationActivity::class.java))
        }
    }

    private fun showFab(isShow: Boolean) {
        if (isShow) {
            binding.relativeMainOverlay.visibility = View.VISIBLE
            binding.fabCreateOrganization.show()
            binding.fabJoinOrganization.show()
            binding.textCreateOrganization.visibility = View.VISIBLE
            binding.textJoinOrganization.visibility = View.VISIBLE

            binding.fabAddOrganization.extend()

            isAllFabVisible = true
        } else {
            binding.relativeMainOverlay.visibility = View.GONE
            binding.fabCreateOrganization.hide()
            binding.fabJoinOrganization.hide()
            binding.textCreateOrganization.visibility = View.GONE
            binding.textJoinOrganization.visibility = View.GONE

            binding.fabAddOrganization.shrink()

            isAllFabVisible = false
        }
    }

}