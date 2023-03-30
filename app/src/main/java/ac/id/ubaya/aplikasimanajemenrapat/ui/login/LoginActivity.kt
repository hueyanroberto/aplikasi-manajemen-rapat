package ac.id.ubaya.aplikasimanajemenrapat.ui.login

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityLoginBinding
import ac.id.ubaya.aplikasimanajemenrapat.ui.main.MainActivity
import ac.id.ubaya.aplikasimanajemenrapat.ui.register.RegisterActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.buttonSignIn.setOnClickListener(this)
        binding.textLoginSignup.setOnClickListener(this)
    }

    private fun login(email: String, password: String) {
        loginViewModel.login(email, password).observe(this) {userResource ->
            when (userResource) {
                is Resource.Loading -> {
                    binding.progressBarLogin.visibility = View.VISIBLE
                    binding.buttonSignIn.setOnClickListener(null)
                    binding.buttonSignIn.setBackgroundResource(R.drawable.button_disable)
                }
                is Resource.Success -> {
                    binding.progressBarLogin.visibility = View.GONE
                    binding.buttonSignIn.setOnClickListener(this)
                    binding.buttonSignIn.setBackgroundResource(R.drawable.button_primary)

                    val user = userResource.data
                    if (user != null) {
                        loginViewModel.saveUserData(user)
                        loginFirebase(email, password)
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        AlertDialog.Builder(this)
                            .setMessage(resources.getString(R.string.email_password_incorrect))
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(this, resources.getString(R.string.internal_error_message), Toast.LENGTH_SHORT).show()

                    binding.progressBarLogin.visibility = View.GONE
                    binding.buttonSignIn.setOnClickListener(this)
                    binding.buttonSignIn.setBackgroundResource(R.drawable.button_primary)
                }
            }
        }
    }

    private fun loginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("LoginActivity", "Login Firebase Success")
                }
            }
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.buttonSignIn.id -> {
                binding.textInputLoginEmail.error = null

                val email = binding.editLoginEmail.text.toString().trim()
                val password = binding.editLoginPassword.text.toString().trim()
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.textInputLoginEmail.error = resources.getString(R.string.invalid_email_format)
                } else {
                    login(email, password)
                }
            }
            binding.textLoginSignup.id -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            binding.buttonSignInGoogle.id -> {
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
            }
        }
    }
}