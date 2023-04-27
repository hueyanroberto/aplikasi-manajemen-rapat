package ac.id.ubaya.aplikasimanajemenrapat.ui.forget

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ActivityForgetPasswordBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgetPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth

        binding.buttonForgetPasswordSubmit.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.buttonForgetPasswordSubmit.id -> {
                binding.textInputForgetPasswordEmail.error = null
                val email = binding.editForgetPasswordEmail.text.toString().trim()
                if (email.isEmpty()) {
                    binding.textInputForgetPasswordEmail.error = resources.getString(R.string.required_field)
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.textInputForgetPasswordEmail.error = resources.getString(R.string.invalid_email_format)
                } else {

                }
            }
        }
    }
}