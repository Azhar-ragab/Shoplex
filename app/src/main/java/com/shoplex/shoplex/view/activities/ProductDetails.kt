package com.shoplex.shoplex.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityProductDetailsBinding
import com.shoplex.shoplex.model.adapter.PagerAdapter

class ProductDetails : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val productId = intent.getStringExtra(getString(R.string.productId))
//        Toast.makeText(this,productId,Toast.LENGTH_SHORT).show()
        var pagerAdapter: PagerAdapter = PagerAdapter(supportFragmentManager, this, productId!!)
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