package ac.id.ubaya.aplikasimanajemenrapat.ui.register

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityRegisterBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.imageRegisterBack.setOnClickListener(this)
        binding.buttonSignUp.setOnClickListener(this)
    }

    private fun register(email: String, password: String) {
        registerViewModel.register(email, password).observe(this) { userResource ->
            when (userResource) {
                is Resource.Loading -> {
                    binding.progressBarRegister.visibility = View.VISIBLE
                    binding.buttonSignUp.setOnClickListener(null)
                    binding.buttonSignUp.setBackgroundResource(R.drawable.button_disable)
                }
                is Resource.Success -> {
                    val user = userResource.data
                    binding.progressBarRegister.visibility = View.GONE
                    binding.buttonSignUp.setOnClickListener(this)
                    binding.buttonSignUp.setBackgroundResource(R.drawable.button_primary)

                    if (user != null) {
                        Log.d("RegisterActivity", user.toString())
                        registerViewModel.saveUserData(user)
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, RegisterNameActivity::class.java))
                        finishAffinity()
                    } else {
                        binding.textInputRegisterEmail.error = resources.getString(R.string.email_used)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(this, resources.getString(R.string.internal_error_message), Toast.LENGTH_SHORT).show()
                    binding.progressBarRegister.visibility = View.GONE
                    binding.buttonSignUp.setOnClickListener(this)
                    binding.buttonSignUp.setBackgroundResource(R.drawable.button_primary)
                }
            }
        }
    }

    private fun registerFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    register(email, password)
                }
            }
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.imageRegisterBack.id -> finish()
            binding.buttonSignUp.id -> {
                binding.textInputRegisterEmail.error = null

                val email = binding.editRegisterEmail.text.toString().trim()
                val password = binding.editRegisterPassword.text.toString().trim()
                val confirmPassword = binding.editRegisterConfirmPassword.text.toString().trim()
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.textInputRegisterEmail.error = resources.getString(R.string.invalid_email_format)
                } else if (password.length < 8) {
                    binding.textInputRegisterPassword.error = resources.getString(R.string.password_length_error)
                } else if (confirmPassword != password) {
                    binding.textInputRegisterConfirmPassword.error = resources.getString(R.string.password_confirm_error)
                } else {
                    registerFirebase(email, password)
                }
            }
        }
    }
}