package ac.id.ubaya.aplikasimanajemenrapat.ui.organization

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityCreateOrganizationBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.login.LoginActivity
import ac.id.ubaya.aplikasimanajemenrapat.utils.bitmapToBase64
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class CreateOrganizationActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val REQUIRED_PERMISSION = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )

        private const val REQUEST_CODE_PERMISSION = 10
    }

    private lateinit var binding: ActivityCreateOrganizationBinding
    private val viewModel: CreateOrganizationViewModel by viewModels()


    private var profilePic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateOrganizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCreateOrganization)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.buttonAddProfileOrganization.setOnClickListener(this)
        binding.buttonCreateOrganization.setOnClickListener(this)

        binding.imageAddProfileOrganization.setImageResource(R.drawable.blank_profile)
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.buttonAddProfileOrganization.id -> {
                if (allPermissionGranted()) {
                    alertDialogChooser()
                } else {
                    ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION)
                }
            }
            binding.buttonCreateOrganization.id -> {
                binding.textInputCreateOrganizationName.error = null
                binding.textInputCreateOrganizationDescription.error = null
                val name = binding.editCreateOrganizationName.text.toString().trim()
                val leaderboardDuration = getSpinnerValue()
                val description = binding.editCreateOrganizationDescription.text.toString().trim()
                if (name.isEmpty()) {
                    binding.textInputCreateOrganizationName.error = resources.getString(R.string.required_field)
                } else if (description.isEmpty()) {
                    binding.textInputCreateOrganizationDescription.error = resources.getString(R.string.required_field)
                } else {
                    createOrganization(name, description, profilePic, leaderboardDuration)
                }
            }
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

    private fun createOrganization(
        name: String,
        description: String,
        profilePic: String,
        leaderboardDuration: Int
    ) {
        viewModel.getUser().observe(this) {
            if (it.id != -1) {
                viewModel.createOrganization(name, description, profilePic, it.token.toString(), leaderboardDuration).observe(this) { organizationResource ->
                    when (organizationResource) {
                        is Resource.Loading -> {
                            binding.progressBarCreateOrganization.visibility = View.VISIBLE
                            binding.buttonCreateOrganization.setOnClickListener(null)
                            binding.buttonCreateOrganization.setBackgroundResource(R.drawable.button_disable)
                        }
                        is Resource.Success -> {
                            binding.progressBarCreateOrganization.visibility = View.GONE
                            binding.buttonCreateOrganization.setOnClickListener(this)
                            binding.buttonCreateOrganization.setBackgroundResource(R.drawable.button_primary)

                            val intent = Intent(this, OrganizationActivity::class.java)
                            intent.putExtra(OrganizationActivity.EXTRA_ORGANIZATION, organizationResource.data)
                            startActivity(intent)
                            finish()
                        }
                        is Resource.Error -> {
                            binding.progressBarCreateOrganization.visibility = View.GONE
                            binding.buttonCreateOrganization.setOnClickListener(this)
                            binding.buttonCreateOrganization.setBackgroundResource(R.drawable.button_primary)

                            Snackbar.make(binding.buttonCreateOrganization, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                                .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                                .setTextColor(resources.getColor(R.color.white, theme))
                                .show()
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
                Toast.makeText(
                    this,
                    resources.getString(R.string.permission_declined),
                    Toast.LENGTH_SHORT
                ).show()
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
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
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
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}