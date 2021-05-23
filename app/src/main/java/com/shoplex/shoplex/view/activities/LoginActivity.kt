package com.shoplex.shoplex.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //firebase Auth
        auth = Firebase.auth

        //Forget Password
        binding.tvForgetPass.setOnClickListener {

        }

        //Not Have Account
        binding.tvCreateAccount.setOnClickListener {
           // startActivity(Intent(this, SignupActivity::class.java))
        }

        //Login button
        binding.btnLogin.setOnClickListener {
            login(binding.edEmail.text.toString(), binding.edPassword.text.toString())
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if (currentUser != null) {
            currentUser.reload()
            reload();
        }
    }

    //login
    private fun login(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(
                        baseContext,
                        getString(R.string.login_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = auth.currentUser
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, getString(R.string.login_fail),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }




    private fun reload() {
        startActivity(Intent(this, HomeActivity::class.java))
    }
}