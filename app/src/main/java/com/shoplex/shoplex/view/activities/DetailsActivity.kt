package com.shoplex.shoplex.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityDetailsBinding
import com.shoplex.shoplex.model.adapter.PagerAdapter
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.viewmodel.DetailsVM
import com.shoplex.shoplex.viewmodel.ProductsVM

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    lateinit var productsVM: ProductsVM
    lateinit var detailsVM: DetailsVM

    override fun onCreate(savedInstanceState: Bundle?) {
        if (UserInfo.lang != this.resources.configuration.locale.language)
            UserInfo.setLocale(UserInfo.lang, this)
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productsVM = ViewModelProvider(this).get(ProductsVM::class.java)
        detailsVM = ViewModelProvider(this).get(DetailsVM::class.java)

        productsVM.productID.value = intent.getStringExtra(getString(R.string.productId))
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.adapter = PagerAdapter(supportFragmentManager, this)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
                title = tab.text
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}