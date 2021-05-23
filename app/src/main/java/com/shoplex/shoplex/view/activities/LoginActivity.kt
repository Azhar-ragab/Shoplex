package com.shoplex.shoplex.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityLoginBinding
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.firebase.UserDBModel
import com.shoplex.shoplex.model.interfaces.INotifyMVP

class LoginActivity : AppCompatActivity(), INotifyMVP {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Forget Password
        binding.tvForgetPass.setOnClickListener {

        }

        //Not Have Account
        binding.tvCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        //Login button
        binding.btnLogin.setOnClickListener {
            login(binding.edEmail.text.toString(), binding.edPassword.text.toString())
        }
    }

    //login
    private fun login(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    UserDBModel(this).getUserByMail(email)

                    //val user = auth.currentUser
                    //startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, getString(R.string.login_fail),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onUserInfoReady() {
        Toast.makeText(applicationContext, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
        UserInfo.updateTokenID()
        finish()
    }

    override fun onUserInfoFailed() {
        Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
    }
}