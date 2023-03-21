package ac.id.ubaya.aplikasimanajemenrapat.ui.organization

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityCreateOrganizationBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import ac.id.ubaya.aplikasimanajemenrapat.utils.bitmapToBase64
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class EditOrganizationActivity: AppCompatActivity(), View.OnClickListener {

    companion object {
        private val REQUIRED_PERMISSION = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )

        private const val REQUEST_CODE_PERMISSION = 10
        const val EXTRA_TOKEN = "extra_token"

        const val EXTRA_ORGANIZATION_NAME = "extra_organization_name"
        const val EXTRA_ORGANIZATION_ID = "extra_organization_id"
        const val EXTRA_ORGANIZATION_DESC = "extra_organization_desc"
        const val EXTRA_ORGANIZATION_PIC = "extra_organization_pic"
        const val EXTRA_ORGANIZATION_DURATION = "extra_organization_duration"

        const val RESULT_CODE = 30
    }

    private lateinit var binding: ActivityCreateOrganizationBinding
    private val viewModel: EditOrganizationViewModel by viewModels()

    private var profilePic = ""
    private var token = ""
    var id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateOrganizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCreateOrganization)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.textCreateOrganizationTitle.text = resources.getString(R.string.edit_organization)
        binding.buttonCreateOrganization.text = resources.getString(R.string.edit_organization)
        binding.buttonAddProfileOrganization.text = resources.getString(R.string.edit_profile_picture)
        binding.textUpdateLeaderboardPeriod.visibility = View.VISIBLE

        token = intent.getStringExtra(EXTRA_TOKEN).toString()
        id = intent.getIntExtra(EXTRA_ORGANIZATION_ID, -1)
        val name = intent.getStringExtra(EXTRA_ORGANIZATION_NAME).toString()
        val desc = intent.getStringExtra(EXTRA_ORGANIZATION_DESC).toString()
        val profilePic = intent.getStringExtra(EXTRA_ORGANIZATION_PIC).toString()
        val duration = intent.getIntExtra(EXTRA_ORGANIZATION_DURATION, 0)

        if (id == -1) {
            Toast.makeText(this, resources.getString(R.string.internal_error_message), Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Glide.with(this)
                .load("$BASE_ASSET_URL/Profile/Organization/$profilePic")
                .error(R.drawable.blank_profile)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.imageAddProfileOrganization)

            binding.editCreateOrganizationName.setText(name)
            binding.editCreateOrganizationDescription.setText(desc)

            when (duration) {
                1 -> binding.spinnerLeaderboaedPeriod.setSelection(0)
                3 -> binding.spinnerLeaderboaedPeriod.setSelection(1)
                6 -> binding.spinnerLeaderboaedPeriod.setSelection(2)
                12 -> binding.spinnerLeaderboaedPeriod.setSelection(3)
                else -> binding.spinnerLeaderboaedPeriod.setSelection(0)
            }

            binding.buttonAddProfileOrganization.setOnClickListener(this)
            binding.buttonCreateOrganization.setOnClickListener(this)
        }
    }

    private fun getSpinnerValue(): Int {
        return when (binding.spinnerLeaderboaedPeriod.selectedItemPosition) {
            0 -> 1
            1 -> 3
            2 -> 6
            3 -> 12
            else -> 0
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (allPermissionGranted()) {
                alertDialogChooser()
            } else {
                Toast.makeText(this, resources.getString(R.string.permission_declined), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun allPermissionGranted(): Boolean {
        var isPermitted = false
        REQUIRED_PERMISSION.forEach { permission ->
            isPermitted = ContextCompat.checkSelfPermission(baseContext, permission) == PackageManager.PERMISSION_GRANTED
        }
        return isPermitted
    }

    private fun alertDialogChooser() {
        val options = arrayOf(
            resources.getString(R.string.take_photo),
            resources.getString(R.string.choose_from_gallery),
            resources.getString(R.string.cancel),
        )

        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.choose_profile_picture))
            .setItems(options) { _, pos ->
                when (pos) {
                    0 -> startCamera()
                    1 -> startGallery()
                }
            }
            .show()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        ac.id.ubaya.aplikasimanajemenrapat.utils.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(this, packageName, it)
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choose_profile_picture))
        launcherIntentGallery.launch(chooser)
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val bitmapResult = BitmapFactory.decodeFile(myFile.path)
            profilePic = bitmapToBase64(bitmapResult)
            binding.imageAddProfileOrganization.setImageBitmap(bitmapResult)

            updateProfilePic()
        }
    }

    @Suppress("DEPRECATION")
    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val imageBitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(contentResolver, selectedImg)
            } else {
                val source = ImageDecoder.createSource(this.contentResolver, selectedImg)
                ImageDecoder.decodeBitmap(source)
            }
            profilePic = bitmapToBase64(imageBitmap)
            binding.imageAddProfileOrganization.setImageBitmap(imageBitmap)

            updateProfilePic()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.buttonAddProfileOrganization.id -> {
                if (allPermissionGranted()) {
                    alertDialogChooser()
                } else {
                    ActivityCompat.requestPermissions(this,
                        REQUIRED_PERMISSION,
                        REQUEST_CODE_PERMISSION
                    )
                }
            }
            binding.buttonCreateOrganization.id -> {
                binding.textInputCreateOrganizationName.error = null
                binding.textInputCreateOrganizationDescription.error = null
                val duration = getSpinnerValue()
                val name = binding.editCreateOrganizationName.text.toString().trim()
                val description = binding.editCreateOrganizationDescription.text.toString().trim()
                if (name.isEmpty()) {
                    binding.textInputCreateOrganizationName.error = resources.getString(R.string.required_field)
                } else if (description.isEmpty()) {
                    binding.textInputCreateOrganizationDescription.error = resources.getString(R.string.required_field)
                } else {
                    editProfile(name, description, duration)
                }
            }
        }
    }

    private fun updateProfilePic() {
        lifecycleScope.launch {
            viewModel.updateOrganizationProfilePic(token, id, profilePic).collect { organizationResponse ->
                when (organizationResponse) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Toast.makeText(
                            this@EditOrganizationActivity,
                            "Profile Updated",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    is Resource.Error -> {
                        Snackbar.make(binding.buttonCreateOrganization, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .show()
                    }
                }
            }
        }
    }

    private fun editProfile(name: String, description: String, duration: Int) {
        lifecycleScope.launch {
            viewModel.editOrganization(token, id, name, description, duration).collect { organizationResource ->
                when (organizationResource) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        organizationResource.data?.let {
                            Log.d("EditOrgAct", it.toString())
                            Toast.makeText(this@EditOrganizationActivity, "Profile Updated", Toast.LENGTH_SHORT ).show()
                            val intent = Intent()
                            intent.putExtra(EXTRA_ORGANIZATION_NAME, it.name)
                            intent.putExtra(EXTRA_ORGANIZATION_DESC, it.description)
                            intent.putExtra(EXTRA_ORGANIZATION_DURATION, it.leaderboardDuration)
                            setResult(RESULT_CODE, intent)
                            finish()
                        }
                    }
                    is Resource.Error -> {
                        Snackbar.make(binding.buttonCreateOrganization, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
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