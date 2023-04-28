package ac.id.ubaya.aplikasimanajemenrapat.ui.register

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityRegisterNameBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.login.LoginActivity
import ac.id.ubaya.aplikasimanajemenrapat.ui.main.MainActivity
import ac.id.ubaya.aplikasimanajemenrapat.utils.bitmapToBase64
import ac.id.ubaya.aplikasimanajemenrapat.utils.createTempFile
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class RegisterNameActivity : AppCompatActivity() {

    companion object {
        private val REQUIRED_PERMISSION = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )

        private val REQUIRED_PERMISSION_TIRAMISU = arrayOf(
            Manifest.permission.CAMERA
        )

        private const val REQUEST_CODE_PERMISSION = 10
    }

    private lateinit var binding: ActivityRegisterNameBinding
    private val viewModel: RegisterNameViewModel by viewModels()
    private lateinit var name: String
    private var profilePic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        binding.imageAddProfile.setImageResource(R.drawable.blank_profile)
        binding.buttonAddProfile.setOnClickListener {
            if (allPermissionGranted()) {
                alertDialogChooser()
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION)
            }
        }

        binding.buttonSaveName.setOnClickListener { buttonSaveClicked() }
    }

    private fun buttonSaveClicked() {
        binding.textInputRegisterName.error = null

        name = binding.editRegisterName.text.toString().trim()
        if (name.isEmpty()) {
            binding.textInputRegisterName.error = resources.getString(R.string.required_field)
        } else {
            addNameAndProfile()
        }
    }

    private fun addNameAndProfile() {
        viewModel.getUser().observe(this) {
            if (it.id != -1 && it.token != null) {
                viewModel.registerName(it.token.toString(), it.id, name, profilePic).observe(this) {userResource ->
                    when (userResource) {
                        is Resource.Loading -> {
                            binding.buttonSaveName.setBackgroundResource(R.drawable.button_disable)
                            binding.buttonSaveName.setOnClickListener(null)
                            binding.progressBarRegisterName.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.progressBarRegisterName.visibility = View.GONE
                            val user = userResource.data
                            if (user != null) {
                                viewModel.saveUserData(user)
                                startActivity(Intent(this, MainActivity::class.java))
                                finishAffinity()
                            } else {
                                binding.buttonSaveName.setBackgroundResource(R.drawable.button_primary)
                                binding.buttonSaveName.setOnClickListener { buttonSaveClicked() }

                                Toast.makeText(this, resources.getString(R.string.internal_error_message), Toast.LENGTH_SHORT).show()
                            }
                        }
                        is Resource.Error -> {
                            binding.buttonSaveName.setBackgroundResource(R.drawable.button_primary)
                            binding.buttonSaveName.setOnClickListener { buttonSaveClicked() }
                            binding.progressBarRegisterName.visibility = View.GONE

                            Toast.makeText(this, resources.getString(R.string.internal_error_message), Toast.LENGTH_SHORT).show()
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

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            buttonSaveClicked()
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

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(this, packageName, it)
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
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
            binding.imageAddProfile.setImageBitmap(bitmapResult)
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
            binding.imageAddProfile.setImageBitmap(imageBitmap)
        }
    }
}