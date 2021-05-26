package com.shoplex.shoplex.view.activities

import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityHomeBinding
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import eg.gov.iti.shoplex.fragments.*

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNavigation
        navController = findNavController(R.id.nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)

    }
    override fun onBackPressed() {
        val seletedItemId = bottomNavigationView.selectedItemId

            if (seletedItemId==R.id.homeFragment2) {
                finishAffinity()
            } else {
                findNavController(R.id.nav_host_fragment).popBackStack()
            }

        }
    }
