package com.shoplex.shoplex.view.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityCheckOutBinding
import com.shoplex.shoplex.model.adapter.CheckoutAdapter
import com.shoplex.shoplex.model.enumurations.DiscountType
import com.shoplex.shoplex.model.pojo.Checkout
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.SpecialDiscount
import com.shoplex.shoplex.viewmodel.CheckoutVM

class CheckOutActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckOutBinding
    var productCart : ArrayList<ProductCart> = arrayListOf()
    var checkout: Checkout = Checkout()
    lateinit var checkoutVM: CheckoutVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarcheckout)
        checkoutVM = ViewModelProvider(this).get(CheckoutVM::class.java)

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
               // binding.viewPagerCheckout.currentItem = tab.position
                //title = tab.text
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        val product1 = Product(
            "T-Shirt",
            100F,
            "Fashion",
            "https://static.zara.net/photos///2021/V/0/1/p/4424/156/800/2/w/167/4424156800_6_1_1.jpg?ts=1612858156056"
        )
        product1.productID = "0b106b7e-4eb4-4935-8dd8-8b68ba56fea8"
        product1.storeID = "be74ee7c-64dd-475f-9c73-b1fbcb7a959b" // azhar@shoplex.com

        val product2 = Product(
            "Shoes",
            100F,
            "Fashion",
            "https://firebasestorage.googleapis.com/v0/b/shoplex-17f64.appspot.com/o/Products%2F1bbce38b-3c0e-4e20-a977-f2eec2c58709%2F38842022-7503-4d8a-aa84-b1c1ef0a64e6?alt=media&token=152bd083-9ab0-4e70-82bf-6216873d8f9d"
        )
        product2.productID = "5c42724e-fc98-41f9-8c6e-7c4f3fc40de8"
        product2.storeID = "be74ee7c-64dd-475f-9c73-b1fbcb7a959b" // heba@gmail.com
        val product3 = Product(
            "Deress",
            500F,
            "Fashion",
            "https://firebasestorage.googleapis.com/v0/b/shoplex-17f64.appspot.com/o/Products%2F0z7lwRpc8MZpDd0KTPuV%2Fdd2078d0-7cd7-4cfb-82c4-1423a2e89a65?alt=media&token=eb6440b8-bf0a-402f-a00c-fadd7353e1fe"
        )
        product3.productID = "bca32a0e-905a-469b-9798-556d90d19d36"
        product3.storeID = "be74ee7c-64dd-475f-9c73-b1fbcb7a959b" // heba@gmail.com

        productCart.add(ProductCart(product1, 1, SpecialDiscount(5f, DiscountType.Fixed), 10)) // 5
        productCart.add(ProductCart(product2, 4, SpecialDiscount(10f, DiscountType.Fixed), 20)) // 10 for each -> 40
        productCart.add( ProductCart(product3, 2, SpecialDiscount(5f, DiscountType.Percentage), 20)) // 25 discount for each -> 50
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