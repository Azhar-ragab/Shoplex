package com.shoplex.shoplex.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityHomeBinding
import com.shoplex.shoplex.databinding.ActivityProductDetailsBinding
import com.shoplex.shoplex.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    val Splash_Screen = 5000
    private lateinit var binding: ActivitySplashBinding
    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    private lateinit var imageView :ImageView
    private lateinit var titleText :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
imageView=binding.imgSplash
        titleText=binding.tvShoplexSplash
        imageView.animation =topAnimation
        titleText.animation=bottomAnimation
        Handler().postDelayed({
val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        },Splash_Screen.toLong())
    }
}