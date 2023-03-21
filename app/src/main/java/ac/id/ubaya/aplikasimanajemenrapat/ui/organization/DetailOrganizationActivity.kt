package ac.id.ubaya.aplikasimanajemenrapat.ui.organization

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityDetailOrganizationBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import ac.id.ubaya.aplikasimanajemenrapat.utils.convertNumber
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class DetailOrganizationActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ORGANIZATION_NAME = "extra_organization_name"
        const val EXTRA_ORGANIZATION_ID = "extra_organization_id"
        const val EXTRA_ORGANIZATION_DESC = "extra_organization_desc"
        const val EXTRA_ORGANIZATION_PIC = "extra_organization_pic"
        const val EXTRA_ORGANIZATION_DURATION = "extra_organization_duration"
        const val EXTRA_ORGANIZATION_PERIOD = "extra_organization_period"
    }

    private lateinit var binding: ActivityDetailOrganizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrganizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val name = intent.getStringExtra(EXTRA_ORGANIZATION_NAME).toString()
        val desc = intent.getStringExtra(EXTRA_ORGANIZATION_DESC).toString()
        val pic = intent.getStringExtra(EXTRA_ORGANIZATION_PIC).toString()
        val duration = intent.getIntExtra(EXTRA_ORGANIZATION_DURATION, 0)
        val period = intent.getIntExtra(EXTRA_ORGANIZATION_PERIOD, 0)

        binding.textOrganizationDetailName.text = name
        binding.textOrganizationDetailDesc.text = desc
        binding.textOrganizationDetailLeaderboardDuration.text = resources.getString(R.string.leaderboard_period_duration, duration.toString())
        binding.textOrganizationDetailLeaderboardPeriod.text = resources.getString(R.string.leaderboard_current_period, convertNumber(period))

        Glide.with(this)
            .load("$BASE_ASSET_URL/Profile/Organization/$pic")
            .error(R.drawable.logo_text_color)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.imageOrganizationDetail)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}