package com.shoplex.shoplex.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivitySignupBinding
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.pojo.Location
import com.shoplex.shoplex.model.pojo.User
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    lateinit var ref: DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ref = FirebaseReferences.usersRef.document()

        binding.btnSignup.setOnClickListener {
            val name = binding.edName.text.toString()
            val email = binding.edEmail.text.toString()
            val phone = binding.edPhone.text.toString()
            val img =
                "https://img.etimg.com/thumb/width-1200,height-900,imgsize-122620,resizemode-1,msid-75214721/industry/services/retail/future-group-negotiates-rents-for-its-1700-stores.jpg"

            if (checkEditText()) {
                createAccount(email, binding.edPassword.text.toString())
                val user =
                    User(
                        ref.id,
                        name,
                        email,
                        Location(0.0, 0.0),
                        "Address",
                        phone,
                        img,
                        arrayListOf(),
                        arrayListOf()
                    )
                addUser(user)
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
        binding.btnLocation.setOnClickListener {
        }

    }

    fun addUser(user: User) {
        ref.set(user).addOnSuccessListener {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        // [END create_user_with_email]
    }

    //check EditText
    fun checkEditText(): Boolean {
        when {
            binding.edName.length() == 0 -> binding.edName.error = getString(R.string.Required)
            binding.edName.length() < 5 -> binding.edName.error =
                getString(R.string.min_client_name_err)
            binding.edEmail.length() == 0 -> binding.edEmail.error = getString(R.string.Required)
            !isEmailValid(binding.edEmail.text.toString()) -> binding.edEmail.error =
                getString(
                    R.string.require_email
                )

            binding.edPassword.length() == 0 -> binding.edPassword.error =
                getString(R.string.Required)
            binding.edPassword.length() < 8 -> binding.edPassword.error =
                getString(R.string.min_password_err)
            binding.edConfirmPassword.length() == 0 -> binding.edConfirmPassword.error =
                getString(R.string.Required)
            binding.edConfirmPassword.text.toString() != binding.edPassword.text.toString() -> binding.edConfirmPassword.error =
                getString(
                    R.string.not_match
                )

            binding.edPhone.length() == 0 -> binding.edPhone.error = getString(R.string.Required)
            else -> return true
        }
        return false
    }

    //Email Validation
    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }
}
