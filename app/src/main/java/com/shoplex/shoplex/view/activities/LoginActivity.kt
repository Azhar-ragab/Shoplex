package com.shoplex.shoplex.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.firestore.DocumentReference
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityLoginBinding
import com.shoplex.shoplex.model.enumurations.AuthType
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.interfaces.INotifyMVP

import com.shoplex.shoplex.viewmodel.AuthVM
import com.shoplex.shoplex.viewmodel.AuthVMFactory
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var callbackManager:CallbackManager
    private lateinit var authVM: AuthVM
    private val RC_SIGN_IN = 120

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authVM = ViewModelProvider(this, AuthVMFactory(this)).get(AuthVM::class.java)
        binding.userData = authVM

        // login with facebook

        binding.btnFace.setReadPermissions(listOf("email", "public_profile"))
        callbackManager = CallbackManager.Factory.create()

        binding.btnFace.setOnClickListener {
            buttonClickLoginFB()
        }

        // Not Have Account
        binding.tvCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
        // Forget Password
        binding.tvForgetPass.setOnClickListener {

        }

        // Login button
        binding.btnLogin.setOnClickListener {
            authVM.login(AuthType.Email)
        }

        binding.btnGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    //login with facebook
    private fun buttonClickLoginFB(){
        val loginManager = LoginManager.getInstance()
        loginManager.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    if(loginResult != null){
                        authVM.login(AuthType.Facebook, loginResult.accessToken.token)
                    }else{
                        Toast.makeText(applicationContext,"Failed",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancel() {
                    Toast.makeText(applicationContext,"User Cancelled it",Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: FacebookException) {

                    Toast.makeText(applicationContext, exception.message,Toast.LENGTH_SHORT).show()

                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode,resultCode,data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    authVM.login(AuthType.Google, account.idToken!!)
                } catch (e: ApiException) {
                    Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}