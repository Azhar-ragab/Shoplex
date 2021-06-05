package com.shoplex.shoplex.view.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityCheckOutBinding
import com.shoplex.shoplex.model.adapter.CartAdapter
import com.shoplex.shoplex.model.adapter.CheckoutAdapter
import com.shoplex.shoplex.model.enumurations.DiscountType
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.maps.LocationManager
import com.shoplex.shoplex.model.pojo.Checkout
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.SpecialDiscount
import com.shoplex.shoplex.viewmodel.CheckoutVM
import kotlin.random.Random

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
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }

        getAllCartProducts()

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
    }

    fun getAllCartProducts() {

        // var cartProducts = ArrayList<ProductCart>()
        FirebaseReferences.usersRef.document(UserInfo.userID!!).get().addOnSuccessListener { result ->
            val cartList: java.util.ArrayList<String> = result.get("cartList") as java.util.ArrayList<String>
            for (productID in cartList){
                FirebaseReferences.productsRef.document(productID).get()
                    .addOnSuccessListener { productResult ->
                        if (productResult != null) {
                            val prod = productResult.toObject<ProductCart>()
                            FirebaseReferences.productsRef.document(productID)
                                .collection("Special Discounts")
                                .document(UserInfo.userID!!).get().addOnSuccessListener {
                                    var specialDiscount: SpecialDiscount? = null
                                    if(it.exists()){
                                        specialDiscount = it.toObject()
                                    }
                                    // LocationManager.getInstance(this).getRouteInfo(UserInfo.location, prod.deliveryLoc)
                                    val productCart = ProductCart(prod!!, Random.nextInt(1, 5), specialDiscount, 10)
                                    // cartProducts.add()

                                    checkout.addProduct(productCart)
                                }
                        }
                    }
            }
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