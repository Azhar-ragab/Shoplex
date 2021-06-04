package com.shoplex.shoplex.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityOrderBinding
import com.shoplex.shoplex.model.adapter.OrderAdapter
import com.shoplex.shoplex.viewmodel.OrdersVM
import androidx.lifecycle.Observer


class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit  var orderAdapter:OrderAdapter
    private lateinit  var lastOrderAdapter:OrderAdapter
    private lateinit var ordersVM: OrdersVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        this.ordersVM= OrdersVM()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarorder)
        supportActionBar?.apply {
            title = getString(R.string.orders)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        }
        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true);
            supportActionBar!!.setDisplayShowHomeEnabled(true);
        }
        ordersVM.getCurrentOrders()
        ordersVM.orders.observe(this, Observer{ orders ->
            orderAdapter = OrderAdapter(orders)
            binding.rvCurrentOrders.adapter = orderAdapter
        })
        ordersVM.getLastOrders()
        ordersVM.lastOrders.observe(this, { lastOrders ->
            lastOrderAdapter = OrderAdapter(lastOrders)
            binding.rvLastOrders.adapter = lastOrderAdapter
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }
}