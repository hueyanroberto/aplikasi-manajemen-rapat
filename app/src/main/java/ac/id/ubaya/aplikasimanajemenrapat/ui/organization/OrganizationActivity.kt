package ac.id.ubaya.aplikasimanajemenrapat.ui.organization

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityOrganizationBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.UserViewModel
import ac.id.ubaya.aplikasimanajemenrapat.ui.login.LoginActivity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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

        organization?.let {
            if (it.role?.id != 3) {
                binding.imageOrganizationMore.visibility = View.VISIBLE
                binding.imageOrganizationMore.setOnClickListener {
                    val popupMenu = PopupMenu(this, it)
                    popupMenu.inflate(R.menu.menu_organization)
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.item_show_code_organization -> {
                                AlertDialog.Builder(this)
                                    .setMessage(resources.getString(R.string.organization_code_data, organization?.code))
                                    .setPositiveButton(resources.getString(R.string.copy_to_clipboard)) { _, _ ->
                                        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Code", organization?.code)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(this, resources.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
                                    }
                                    .show()
                            }
                            R.id.item_edit_organization -> {
                                val intent = Intent(this, EditOrganizationActivity::class.java)
                                intent.putExtra(EditOrganizationActivity.EXTRA_TOKEN, user?.token.toString())
                                intent.putExtra(EditOrganizationActivity.EXTRA_ORGANIZATION_NAME, organization?.name)
                                intent.putExtra(EditOrganizationActivity.EXTRA_ORGANIZATION_ID, organization?.id)
                                intent.putExtra(EditOrganizationActivity.EXTRA_ORGANIZATION_DESC, organization?.description)
                                intent.putExtra(EditOrganizationActivity.EXTRA_ORGANIZATION_PIC, organization?.profilePicture)
                                launcherIntent.launch(intent)
                            }
                        }
                        true
                    }
                    popupMenu.show()
                }
            }
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
                val argumentRoleId = NavArgument.Builder().setDefaultValue(organization?.role?.id ?: -1).build()

                destinationMeeting?.addArgument("token", argumentToken)
                destinationMeeting?.addArgument("organizationId", argumentOrganizationId)
                destinationMeeting?.addArgument("roleId", argumentRoleId)

                destinationMember?.addArgument("token", argumentToken)
                destinationMember?.addArgument("organizationId", argumentOrganizationId)
                destinationMember?.addArgument("roleId", argumentRoleId)

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

    private val launcherIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.data != null) {
            if (result.resultCode == EditOrganizationActivity.RESULT_CODE) {
                val orgName = result.data?.getStringExtra(EditOrganizationActivity.EXTRA_ORGANIZATION_NAME).toString()
                val orgDesc = result.data?.getStringExtra(EditOrganizationActivity.EXTRA_ORGANIZATION_DESC).toString()

                organization?.name = orgName
                organization?.description = orgDesc

                binding.textOrganizationTitle.text = organization?.name.toString()
            }
        }
    }
}