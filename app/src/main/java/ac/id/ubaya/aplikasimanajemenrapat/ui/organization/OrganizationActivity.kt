package ac.id.ubaya.aplikasimanajemenrapat.ui.organization

import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityOrganizationBinding
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrganizationActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ORGANIZATION = "extra_organization"
    }

    private lateinit var binding: ActivityOrganizationBinding
    private var organization: Organization? = null

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
    }
}