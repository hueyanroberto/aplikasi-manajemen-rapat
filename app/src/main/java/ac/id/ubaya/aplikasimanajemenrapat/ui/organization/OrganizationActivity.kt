package ac.id.ubaya.aplikasimanajemenrapat.ui.organization

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityOrganizationBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.UserViewModel
import ac.id.ubaya.aplikasimanajemenrapat.ui.login.LoginActivity
import android.content.Intent
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrganizationActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ORGANIZATION = "extra_organization"
    }

    private lateinit var binding: ActivityOrganizationBinding
    private var organization: Organization? = null

    private val viewModel: UserViewModel by viewModels()
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrganizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        organization = if (VERSION.SDK_INT > 33) {
            intent.getParcelableExtra(EXTRA_ORGANIZATION, Organization::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_ORGANIZATION)
        }

        init()
    }

    private fun init() {
        binding.textOrganizationTitle.text = organization?.name
        binding.imageOrganizationBack.setOnClickListener { finish() }
        viewModel.isUserGet.observe(this) {userGet ->
            if (userGet) {
                val bundle = bundleOf("token" to user?.token)
                organization?.id?.let { bundle.putInt("organization_id", it) }

                val navController = findNavController(R.id.nav_host_fragment_organization)

                val navGraph = navController.navInflater.inflate(R.navigation.organization_navigation)

                val destinationMeeting = navGraph.findNode(R.id.navigation_meetings)
                val destinationMember = navGraph.findNode(R.id.navigation_members)
                val destinationLeaderboard = navGraph.findNode(R.id.navigation_leaderboard)

                val argumentToken = NavArgument.Builder().setDefaultValue(user?.token ?: "").build()
                val argumentOrganizationId = NavArgument.Builder().setDefaultValue(organization?.id ?: -1).build()

                destinationMeeting?.addArgument("token", argumentToken)
                destinationMeeting?.addArgument("organizationId", argumentOrganizationId)
                destinationMember?.addArgument("token", argumentToken)
                destinationMember?.addArgument("organizationId", argumentOrganizationId)
                destinationLeaderboard?.addArgument("token", argumentToken)
                destinationLeaderboard?.addArgument("organizationId", argumentOrganizationId)

                navController.graph = navGraph

                binding.bottomNavOrganization.setupWithNavController(navController)

            }
        }

        viewModel.getUser().observe(this) { user ->
            if (user.id != -1) {
                this.user = user
                viewModel.changeGetUserStatus()
            } else {
                Toast.makeText(this, resources.getString(R.string.user_not_login_error), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
        }
    }
}