package ac.id.ubaya.aplikasimanajemenrapat.ui.organization

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityJoinOrganizationBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.login.LoginActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinOrganizationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityJoinOrganizationBinding
    private val viewModel: JoinOrganizationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinOrganizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageJoinOrganizationBack.setOnClickListener(this)
        binding.buttonJoinOrganization.setOnClickListener(this)
    }

    private fun joinOrganization(code: String) {
        viewModel.getUser().observe(this) {
            if (it.id != -1) {
                viewModel.joinOrganization(it.id, code).observe(this) { organizationResource ->
                    when (organizationResource) {
                        is Resource.Loading -> {
                            binding.progressBarJoinOrganization.visibility = View.VISIBLE
                            binding.buttonJoinOrganization.setOnClickListener(null)
                            binding.buttonJoinOrganization.setBackgroundResource(R.drawable.button_disable)
                        }
                        is Resource.Success -> {
                            binding.progressBarJoinOrganization.visibility = View.GONE
                            binding.buttonJoinOrganization.setOnClickListener(this)
                            binding.buttonJoinOrganization.setBackgroundResource(R.drawable.button_primary)

                            if (organizationResource.data != null) {
                                val intent = Intent(this, OrganizationActivity::class.java)
                                intent.putExtra(OrganizationActivity.EXTRA_ORGANIZATION, organizationResource.data)
                                startActivity(intent)
                                finish()
                            } else {
                                binding.textInputJoinOrganizationCode.error = resources.getString(R.string.organization_joined)
                            }
                        }
                        is Resource.Error -> {
                            binding.progressBarJoinOrganization.visibility = View.GONE
                            binding.buttonJoinOrganization.setOnClickListener(this)
                            binding.buttonJoinOrganization.setBackgroundResource(R.drawable.button_primary)

                            if (organizationResource.message == "not found") {
                                binding.textInputJoinOrganizationCode.error = resources.getString(R.string.organization_not_found)
                            } else {
                                Snackbar.make(binding.buttonJoinOrganization, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                                    .setTextColor(resources.getColor(R.color.white, theme))
                                    .show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, resources.getString(R.string.user_not_login_error), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.imageJoinOrganizationBack.id -> finish()
            binding.buttonJoinOrganization.id -> {
                binding.textInputJoinOrganizationCode.error = null
                val code = binding.editJoinOrganizationCode.text.toString().trim()

                if (code.isEmpty()) {
                    binding.textInputJoinOrganizationCode.error = resources.getString(R.string.required_field)
                } else if (code.length != 7) {
                    binding.textInputJoinOrganizationCode.error = resources.getString(R.string.organization_code_length)
                } else {
                    joinOrganization(code)
                }
            }
        }
    }
}