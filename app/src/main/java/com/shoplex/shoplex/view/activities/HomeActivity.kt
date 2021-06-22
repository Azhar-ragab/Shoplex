package com.shoplex.shoplex.view.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityHomeBinding
import com.shoplex.shoplex.model.extra.UserInfo

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(UserInfo.userID == null)
            UserInfo.readUserInfo(this)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNavigation
        navController = findNavController(R.id.nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)

    }

    override fun onBackPressed() {

        if (bottomNavigationView.selectedItemId == R.id.homeFragment2) {
            finishAffinity()
        } else {
            findNavController(R.id.nav_host_fragment).popBackStack()
        }
    }
}
