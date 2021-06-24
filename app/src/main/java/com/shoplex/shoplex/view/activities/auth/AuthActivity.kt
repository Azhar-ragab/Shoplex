package com.shoplex.shoplex.view.activities.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityAuthBinding
import com.shoplex.shoplex.model.enumurations.AuthType
import com.shoplex.shoplex.model.extra.ArchLifecycleApp
import com.shoplex.shoplex.viewmodel.AuthVM
import com.shoplex.shoplex.viewmodel.AuthVMFactory
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    lateinit var authVM: AuthVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authVM = ViewModelProvider(this, AuthVMFactory(this)).get(AuthVM::class.java)
        binding.tabLatout.setupWithViewPager(binding.viewPager)
        binding.viewPager.adapter = AuthAdapter(supportFragmentManager, this)

        binding.tabLatout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
                title = tab.text
                btnLogin.text = title
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.btnLogin.setOnClickListener {
            if (ArchLifecycleApp.isInternetConnected) {
                authVM.isLoginValid.value = false
                authVM.isSignupValid.value = false
                if (binding.viewPager.currentItem == 0) {
                    authVM.isLoginBtnPressed.value = true
                    authVM.isLoginValid.observe(this, {
                        if (it) {
                            authVM.login(AuthType.Email)
                            authVM.isLoginValid.value = false
                        }
                    })
                } else {
                    authVM.isSignupBtnPressed.value = true
                    authVM.isSignupValid.observe(this, {
                        if (it) {
                            authVM.createAccount()
                            finish()
                            authVM.isSignupValid.value = false
                        }
                    })
                }
            } else{
                val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.NoInternetConnection), Snackbar.LENGTH_LONG)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
                snackbar.show()
            }
        }
    }
}