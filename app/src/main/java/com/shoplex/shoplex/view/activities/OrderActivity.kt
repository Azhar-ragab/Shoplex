package com.shoplex.shoplex.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.shoplex.shoplex.Orders
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityOrderBinding
import com.shoplex.shoplex.model.adapter.OrderAdapter
import com.shoplex.shoplex.model.enumurations.OrderStatus

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit  var orderAdapter:OrderAdapter
    private lateinit  var lastOrderAdapter:OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarorder)
        supportActionBar?.apply {
            title = getString(R.string.orders)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        }
        if (getSupportActionBar() != null){
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        }
        val currorder = ArrayList<Orders>()
        currorder.add(Orders("",OrderStatus.CURRENT,"Diamond",10.0F,"Fashion","https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
        currorder.add(Orders("",OrderStatus.CURRENT,"Diamond",10.0F,"Fashion","https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
        currorder.add(Orders("",OrderStatus.CURRENT,"Diamond",10.0F,"Fashion","https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
        currorder.add(Orders("",OrderStatus.CURRENT,"Diamond",10.0F,"Fashion","https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
        currorder.add(Orders("",OrderStatus.CURRENT,"Diamond",10.0F,"Fashion","https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))

        val lastorder = ArrayList<Orders>()
        lastorder.add(Orders("",OrderStatus.DELIVERD,"Diamond",10.0F,"Fashion","https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
        lastorder.add(Orders("",OrderStatus.CANCEL,"Diamond",10.0F,"Fashion","https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
        lastorder.add(Orders("",OrderStatus.DELIVERD,"Diamond",10.0F,"Fashion","https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
        lastorder.add(Orders("",OrderStatus.CANCEL,"Diamond",10.0F,"Fashion","https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
        lastorder.add(Orders("",OrderStatus.DELIVERD,"Diamond",10.0F,"Fashion","https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))


        orderAdapter = OrderAdapter(currorder)
        binding.rvCurrentOrders.adapter = orderAdapter
        lastOrderAdapter = OrderAdapter(lastorder)
        binding.rvLastOrders.adapter = lastOrderAdapter


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }
}