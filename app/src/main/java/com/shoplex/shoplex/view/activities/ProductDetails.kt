package com.shoplex.shoplex.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil

import com.google.android.material.tabs.TabLayout
import com.shoplex.shoplex.PagerAdapter
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityProductDetailsBinding

class ProductDetails : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details)


        var pagerAdapter: PagerAdapter = PagerAdapter(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.adapter = pagerAdapter
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