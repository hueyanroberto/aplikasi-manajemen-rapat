package ac.id.ubaya.aplikasimanajemenrapat.ui.profile.update

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityUpdateProfileBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.UserViewModel
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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class UpdateProfileActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val REQUIRED_PERMISSION = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )

        private val REQUIRED_PERMISSION_TIRAMISU = arrayOf(
            Manifest.permission.CAMERA
        )

        private const val REQUEST_CODE_PERMISSION = 10
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_PROFILE_PIC = "extra_profile_pic"
        const val EXTRA_NAME = "extra_name"
    }

    private lateinit var binding: ActivityUpdateProfileBinding
    private val viewModel: UpdateProfileViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private var profilePic = ""
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        token = intent.getStringExtra(EXTRA_TOKEN).toString()
        val profilePic = intent.getStringExtra(EXTRA_PROFILE_PIC).toString()
        val name = intent.getStringExtra(EXTRA_NAME).toString()
        binding.editUpdateProfileName.setText(name)
        Glide.with(this)
            .load("$BASE_ASSET_URL/Profile/User/${profilePic}")
            .error(R.drawable.blank_profile)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.circleImageView)

        binding.buttonUpdateProfile.setOnClickListener(this)
        binding.buttonUpdateProfilePicture.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.buttonUpdateProfile.id -> {
                binding.textInputUpdateProfileName.error = null
                val name = binding.editUpdateProfileName.text.toString().trim()
                if (name.isEmpty()) {
                    binding.textInputUpdateProfileName.error = resources.getString(R.string.required_field)
                } else {
                    updateProfile(name)
                }
            }
            binding.buttonUpdateProfilePicture.id -> {
                if (allPermissionGranted()) {
                    alertDialogChooser()
                } else {
                    ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            REQUIRED_PERMISSION_TIRAMISU.forEach { permission ->
                isPermitted = ContextCompat.checkSelfPermission(baseContext, permission) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            REQUIRED_PERMISSION.forEach { permission ->
                isPermitted = ContextCompat.checkSelfPermission(baseContext, permission) == PackageManager.PERMISSION_GRANTED
            }
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
            binding.circleImageView.setImageBitmap(bitmapResult)

            updateProfilePic(profilePic)
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
            binding.circleImageView.setImageBitmap(imageBitmap)

            updateProfilePic(profilePic)
        }
    }

    private fun updateProfilePic(profilePic: String) {
        lifecycleScope.launch {
            viewModel.updateProfilePic(token, profilePic).collect {userResource ->
                when (userResource) {
                    is Resource.Loading -> {
                        binding.progressBarUpdateProfile.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        val user = userResource.data
                        user?.let {
                            userViewModel.changeProfile(it.profilePic.toString())
                        }
                        binding.progressBarUpdateProfile.visibility = View.GONE
                        Toast.makeText(this@UpdateProfileActivity, getString(R.string.profile_updated), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is Resource.Error -> {
                        binding.progressBarUpdateProfile.visibility = View.VISIBLE
                        Snackbar.make(binding.buttonUpdateProfile, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .show()
                    }
                }
            }
        }
    }

    private fun updateProfile(name: String) {
        lifecycleScope.launch {
            viewModel.updateProfile(token, name).collect {userResource ->
                when (userResource) {
                    is Resource.Loading -> {
                        binding.progressBarUpdateProfile.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarUpdateProfile.visibility = View.GONE
                        userViewModel.changeName(name)
                        Toast.makeText(this@UpdateProfileActivity, getString(R.string.profile_updated), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is Resource.Error -> {
                        binding.progressBarUpdateProfile.visibility = View.VISIBLE
                        Snackbar.make(binding.buttonUpdateProfile, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.secondary_dark, theme))
                            .setTextColor(resources.getColor(R.color.white, theme))
                            .show()
                    }
                }
            }
        }
    }
}