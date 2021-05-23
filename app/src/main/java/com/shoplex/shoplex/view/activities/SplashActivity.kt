package com.shoplex.shoplex.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivitySplashBinding
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.firebase.UserDBModel
import com.shoplex.shoplex.model.interfaces.INotifyMVP

class SplashActivity : AppCompatActivity(), INotifyMVP {
    val Splash_Screen = 4000
    private lateinit var binding: ActivitySplashBinding
    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentUser = Firebase.auth.currentUser
        if(currentUser != null){
            currentUser?.reload()
            UserDBModel(null).getUserByMail(currentUser!!.email)
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        binding.imgSplash.animation = topAnimation
        binding.tvShoplexSplash.animation = bottomAnimation

        Handler().postDelayed({
            currentUser = Firebase.auth.currentUser

            if (currentUser == null) {
                UserInfo.clear()
            }

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, Splash_Screen.toLong())
    }
}