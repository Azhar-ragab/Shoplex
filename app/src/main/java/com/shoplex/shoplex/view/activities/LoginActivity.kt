package com.shoplex.shoplex.view.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityLoginBinding
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.firebase.UserDBModel
import com.shoplex.shoplex.model.interfaces.INotifyMVP
import com.shoplex.shoplex.model.pojo.Location
import com.shoplex.shoplex.model.pojo.User
import java.security.MessageDigest
import java.util.*


class LoginActivity : AppCompatActivity(), INotifyMVP {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var ref: DocumentReference
    lateinit var callbackManager:CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ref = FirebaseReferences.usersRef.document()
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        // login with facebook
        binding.btnFace.setReadPermissions("email", "public_profile")
        binding.btnFace.setOnClickListener {
            buttonClickLoginFB()
        }


        //Not Have Account
        binding.tvCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
        //Forget Password
        binding.tvForgetPass.setOnClickListener {

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
                    UserDBModel(this).getUserByMail(email,false)

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
        startActivity(Intent(this, HomeActivity::class.java))
        Toast.makeText(applicationContext, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
        UserInfo.updateTokenID()
        finish()
    }

    override fun onUserInfoFailed() {
        Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
    }


    //login with facebook
    private fun buttonClickLoginFB(){
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {

                    handleFaceBookLogin(loginResult?.accessToken)
                }

                override fun onCancel() {

                    Toast.makeText(applicationContext,"User Cancelled it",Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: FacebookException) {

                    Toast.makeText(applicationContext, exception.message,Toast.LENGTH_SHORT).show()

                }
            })
    }

    private fun handleFaceBookLogin(accessToken: AccessToken?) {

        val authCredential:AuthCredential=FacebookAuthProvider.getCredential(accessToken!!.token)
        Firebase.auth.signInWithCredential(authCredential).addOnCompleteListener( OnCompleteListener<AuthResult>(){task ->
            if(task.isSuccessful) run {
                val user: FirebaseUser = Firebase.auth.currentUser
                UserDBModel(this).getUserByMail(user.email,true)

            }else{
                Toast.makeText(applicationContext, "couldn`t register to firebase",Toast.LENGTH_SHORT).show()

            }


        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       callbackManager.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onNewFacebookAccount() {
        val userface: FirebaseUser = Firebase.auth.currentUser
        val user =
            User(
                ref.id,
                userface.displayName,
                userface.email,
                Location(0.0, 0.0),
                "Address",
                "01016512198",
                userface.photoUrl.toString(),
                arrayListOf(),
                arrayListOf()
            )
        addUser(user)

    }
    fun addUser(user: User) {
        ref.set(user).addOnSuccessListener {

            UserInfo.userID = user.userID
            UserInfo.image = user.image
            UserInfo.name = user.name
            UserInfo.image = user.image
            UserInfo.email = user.email
            UserInfo.location = user.location
            UserInfo.address = user.address
            UserInfo.phone = user.phone
            UserInfo.favouriteList = user.favouriteList
            UserInfo.cartList = user.cartList
            UserInfo.updateTokenID()
            startActivity(Intent(this, HomeActivity::class.java))
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }
    }




}