package com.shoplex.shoplex.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivitySignupBinding
import com.shoplex.shoplex.viewmodel.UserVM
import com.shoplex.shoplex.viewmodel.UsersVMFactory
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var userVM: UserVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userVM = ViewModelProvider(this, UsersVMFactory(this)).get(UserVM::class.java)
        binding.userData = userVM
        binding.btnSignup.setOnClickListener {
            val img = "https://img.etimg.com/thumb/width-1200,height-900,imgsize-122620,resizemode-1,msid-75214721/industry/services/retail/future-group-negotiates-rents-for-its-1700-stores.jpg"
            userVM.user.value?.image ?: img
            userVM.createAccount()
            if (validateInput()) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
        binding.btnLocation.setOnClickListener {
        }

    }

    //check EditText
    private fun validateInput(): Boolean {
        when {
            binding.edName.length() == 0 -> binding.edName.error = getString(R.string.Required)
            binding.edName.length() < 5 -> binding.edName.error =
                getString(R.string.min_client_name_err)
            binding.edEmail.length() == 0 -> binding.edEmail.error = getString(R.string.Required)
            !isEmailValid(binding.edEmail.text.toString()) -> binding.edEmail.error =
                getString(R.string.require_email)
            binding.edPassword.length() == 0 -> binding.edPassword.error =
                getString(R.string.Required)
            binding.edPassword.length() < 8 -> binding.edPassword.error =
                getString(R.string.min_password_err)
            binding.edConfirmPassword.length() == 0 -> binding.edConfirmPassword.error =
                getString(R.string.Required)
            binding.edConfirmPassword.text.toString() != binding.edPassword.text.toString() -> binding.edConfirmPassword.error =
                getString(R.string.not_match)

            binding.edPhone.length() == 0 -> binding.edPhone.error = getString(R.string.Required)
            else -> return true
        }
        return false
    }

    //Email Validation
    private fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }
}
