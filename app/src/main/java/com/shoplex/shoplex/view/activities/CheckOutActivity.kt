package com.shoplex.shoplex.view.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FieldValue
import com.google.gson.Gson
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityCheckOutBinding
import com.shoplex.shoplex.model.adapter.CheckoutAdapter
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Order
import com.shoplex.shoplex.model.pojo.ProductQuantity
import com.shoplex.shoplex.room.data.ShopLexDataBase
import com.shoplex.shoplex.room.repository.FavoriteCartRepo
import com.shoplex.shoplex.view.fragments.SummaryFragment
import com.shoplex.shoplex.viewmodel.CheckoutFactory
import com.shoplex.shoplex.viewmodel.CheckoutVM
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

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
            intent.getParcelableArrayListExtra<ProductQuantity>(PRODUCTS_QUANTITY) as ArrayList<ProductQuantity>

        if(intent.hasExtra(PRODUCT_PROPERTIES))
            checkoutVM.productProperties = intent.getStringArrayListExtra(PRODUCT_PROPERTIES)

        supportActionBar?.apply {
            title = getString(R.string.Checkout)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        if(checkoutVM.getAllProducts().isNullOrEmpty()) {
            if (!intent.hasExtra("isBuyNow")) {
                checkoutVM.getAllCartProducts()
            } else {
                val product = checkoutVM.productQuantities.firstOrNull()
                if (product != null)
                    checkoutVM.getProductByID(product.productID)
                else
                    Toast.makeText(this, "Product Not Found!", Toast.LENGTH_SHORT).show()
            }
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

    companion object{
        const val PRODUCTS_QUANTITY = "PRODUCTS_QUANTITY"
        const val PRODUCT_PROPERTIES = "PRODUCT_PROPERTIES"
    }
}