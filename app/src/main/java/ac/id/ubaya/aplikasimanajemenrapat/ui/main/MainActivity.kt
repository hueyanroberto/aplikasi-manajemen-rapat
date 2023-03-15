package ac.id.ubaya.aplikasimanajemenrapat.ui.main

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.databinding.DrawerHeaderBinding
import ac.id.ubaya.aplikasimanajemenrapat.databinding.MainLayoutBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.login.LoginActivity
import ac.id.ubaya.aplikasimanajemenrapat.ui.organization.CreateOrganizationActivity
import ac.id.ubaya.aplikasimanajemenrapat.ui.organization.JoinOrganizationActivity
import ac.id.ubaya.aplikasimanajemenrapat.ui.profile.ProfileActivity
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainLayoutBinding
    private lateinit var navBinding: DrawerHeaderBinding

    private val mainViewModel: MainViewModel by viewModels()
    private var isAllFabVisible = false
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navBinding = DrawerHeaderBinding.inflate(LayoutInflater.from(this), binding.root, false)
        binding.navMain.addHeaderView(navBinding.root)

        setUpFab()
        
        binding.mainActivity.recyclerOrganizations.layoutManager = LinearLayoutManager(this)

        binding.mainActivity.refreshMain.setOnRefreshListener {
            binding.mainActivity.refreshMain.isRefreshing = false
        }

        init()
    }

    private fun init() {
        mainViewModel.isUserGet.observe(this) { isUserGet ->
            if (isUserGet) {
                getListOrganization()

                binding.mainActivity.refreshMain.setOnRefreshListener {
                    getListOrganization()
                }

                binding.mainActivity.imageLogOut.setOnClickListener {
                    mainViewModel.logOut(user?.token.toString())
                }

                setUpNavigationDrawer()
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

    private fun setUpNavigationDrawer() {
        navBinding.textDrawerEmail.text = user?.email
        navBinding.textDrawerName.text = user?.name
        Glide.with(this)
            .load("$BASE_ASSET_URL/Asset/Profile/User/${user?.profilePic}")
            .error(R.drawable.blank_profile)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(navBinding.imageDrawerProfile)

        val drawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.mainActivity.toolbarMain, R.string.app_name, R.string.app_name)
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()

        binding.navMain.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_drawer_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
                R.id.menu_drawer_settings ->{}
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun getListOrganization() {
        Log.d("MainActivity", "getListOrganization: Method dipanggil")
        user?.let {
            mainViewModel.getListOrganization(it.token.toString()).observe(this) { organizationResponse ->
                when (organizationResponse) {
                    is Resource.Loading -> {
                        binding.mainActivity.progressBarMain.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.mainActivity.progressBarMain.visibility = View.GONE
                        binding.mainActivity.refreshMain.isRefreshing = false

                        val organization = organizationResponse.data
                        if (organization != null) {
                            val adapter = OrganizationAdapter(organization)
                            binding.mainActivity.recyclerOrganizations.adapter = adapter
                        }
                    }
                    is Resource.Error -> {
                        binding.mainActivity.progressBarMain.visibility = View.GONE
                        binding.mainActivity.refreshMain.isRefreshing = false

                        Snackbar.make(binding.mainActivity.fabAddOrganization, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
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
        binding.mainActivity.relativeMainOverlay.visibility = View.GONE
        binding.mainActivity.fabCreateOrganization.visibility = View.GONE
        binding.mainActivity.fabJoinOrganization.visibility = View.GONE
        binding.mainActivity.textCreateOrganization.visibility = View.GONE
        binding.mainActivity.textJoinOrganization.visibility = View.GONE

        binding.mainActivity.fabAddOrganization.shrink()

        binding.mainActivity.fabAddOrganization.setOnClickListener {
            showFab(!isAllFabVisible)
        }

        binding.mainActivity.relativeMainOverlay.setOnClickListener {
            showFab(false)
        }

        binding.mainActivity.fabCreateOrganization.setOnClickListener {
            showFab(false)
            startActivity(Intent(this, CreateOrganizationActivity::class.java))
        }

        binding.mainActivity.fabJoinOrganization.setOnClickListener {
            showFab(false)
            startActivity(Intent(this, JoinOrganizationActivity::class.java))
        }
    }

    private fun showFab(isShow: Boolean) {
        if (isShow) {
            binding.mainActivity.relativeMainOverlay.visibility = View.VISIBLE
            binding.mainActivity.fabCreateOrganization.show()
            binding.mainActivity.fabJoinOrganization.show()
            binding.mainActivity.textCreateOrganization.visibility = View.VISIBLE
            binding.mainActivity.textJoinOrganization.visibility = View.VISIBLE

            binding.mainActivity.fabAddOrganization.extend()

            isAllFabVisible = true
        } else {
            binding.mainActivity.relativeMainOverlay.visibility = View.GONE
            binding.mainActivity.fabCreateOrganization.hide()
            binding.mainActivity.fabJoinOrganization.hide()
            binding.mainActivity.textCreateOrganization.visibility = View.GONE
            binding.mainActivity.textJoinOrganization.visibility = View.GONE

            binding.mainActivity.fabAddOrganization.shrink()

            isAllFabVisible = false
        }
    }

}