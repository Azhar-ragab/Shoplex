package com.shoplex.shoplex.view.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityCheckOutBinding
import com.shoplex.shoplex.model.adapter.CheckoutAdapter
import com.shoplex.shoplex.model.pojo.ProductQuantity
import com.shoplex.shoplex.viewmodel.CheckoutFactory
import com.shoplex.shoplex.viewmodel.CheckoutVM

class CheckOutActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckOutBinding
    lateinit var checkoutVM: CheckoutVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarcheckout)
        checkoutVM = ViewModelProvider(this, CheckoutFactory(this)).get(CheckoutVM::class.java)
        checkoutVM.productQuantities =
            intent.getParcelableArrayListExtra<ProductQuantity>("PRODUCTS_QUANTITY") as ArrayList<ProductQuantity>

        supportActionBar?.apply {
            title = getString(R.string.Checkout)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        if (!intent.hasExtra("isBuyNow")) {
            checkoutVM.getAllCartProducts()
        } else {
            val product = checkoutVM.productQuantities.firstOrNull()
            if (product != null)
                checkoutVM.getProductByID(product.productID)
            else
                Toast.makeText(this, "Product Not Found!", Toast.LENGTH_SHORT).show()
        }

        val checkoutAdapter = CheckoutAdapter(this, supportFragmentManager)
        binding.tabLayoutCheckout.setupWithViewPager(binding.viewPagerCheckout)

        binding.viewPagerCheckout.adapter = checkoutAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}