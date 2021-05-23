package com.shoplex.shoplex.view.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.tabs.TabLayout
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityCheckOutBinding
import com.shoplex.shoplex.model.adapter.CheckoutAdapter
import com.shoplex.shoplex.model.enumurations.DiscountType
import com.shoplex.shoplex.model.pojo.Checkout
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.SpecialDiscount


class CheckOutActivity : AppCompatActivity() {
   private lateinit var binding: ActivityCheckOutBinding
    var productCart : ArrayList<ProductCart> = arrayListOf()
    var checkout: Checkout = Checkout()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarcheckout)

       //val cartProducts = intent.getParcelableArrayListExtra<ProductCart>("checkout")
        //Toast.makeText(this,cartProducts.toString(),Toast.LENGTH_SHORT).show()

        supportActionBar?.apply {
            title = getString(R.string.Checkout)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        }
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        }

        var checkoutAdapter: CheckoutAdapter = CheckoutAdapter(supportFragmentManager)
        binding.tabLayoutCheckout.setupWithViewPager(binding.viewPagerCheckout)


        binding.viewPagerCheckout.adapter = checkoutAdapter
        binding.tabLayoutCheckout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPagerCheckout.currentItem = tab.position
                title = tab.text
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        val product1 = Product(
            "T-Shirt",
            30F,
            "Fashion",
            LatLng(0.0, 0.0),
            "https://static.zara.net/photos///2021/V/0/1/p/4424/156/800/2/w/167/4424156800_6_1_1.jpg?ts=1612858156056"
        )
        val product2 = Product(
            "Shoes",
            100F,
            "Fashion",
            LatLng(0.0, 0.0),
            "https://static.zara.net/photos///2021/V/0/1/p/4424/156/800/2/w/167/4424156800_6_1_1.jpg?ts=1612858156056"
        )
        val product3 = Product(
            "Deress",
            500F,
            "Fashion",
            LatLng(0.0, 0.0),
            "https://static.zara.net/photos///2021/V/0/1/p/4424/156/800/2/w/167/4424156800_6_1_1.jpg?ts=1612858156056"
        )
        productCart.add(ProductCart(product1, 1, SpecialDiscount(5f, DiscountType.Fixed)))
        productCart.add(ProductCart(product2, 4, SpecialDiscount(10f, DiscountType.Percentage)))
        productCart.add( ProductCart(product3, 1, SpecialDiscount(5f, DiscountType.Fixed)))
       // Toast.makeText(this,productCart[0].quantity.toString(),Toast.LENGTH_SHORT).show()

        for (item in productCart){
            checkout.addProduct(item)
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }
}