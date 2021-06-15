package com.shoplex.shoplex.view.activities

import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityOrderBinding
import com.shoplex.shoplex.model.adapter.OrderAdapter
import com.shoplex.shoplex.viewmodel.OrdersVM
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.databinding.DialogAddReviewBinding
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Review


class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit  var orderAdapter: OrderAdapter
    private lateinit  var lastOrderAdapter: OrderAdapter
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
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
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

        val notificationManager = this.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        if(intent.hasExtra("isNoti")) {
            val productId = intent.getStringExtra("productID")
            notificationManager.cancel(100)
            if(productId != null)
            showAddReviewDialog(productId)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAddReviewDialog(productId: String) {
        val binding = DialogAddReviewBinding.inflate(LayoutInflater.from(binding.root.context))
        val reviewBtnSheetDialog = BottomSheetDialog(binding.root.context)
        reviewBtnSheetDialog.setContentView(binding.root)

        FirebaseReferences.productsRef.document(productId).collection("Reviews").document(UserInfo.userID!!).get().addOnSuccessListener {
            if(it.exists()){
                val review: Review = it.toObject()!!
                binding.rbAddReview.rating = review.rate
                binding.edReview.setText(review.comment)
                binding.btnSendReview.text = "Update Review"
            }
            reviewBtnSheetDialog.show()
        }

        binding.btnSendReview.setOnClickListener {
            //val numStats = binding.rbAddReview.numStars
            val rate: Float = binding.rbAddReview.rating
            val reviewMsg = binding.edReview.text.toString()
            val review = Review(
                UserInfo.name,
                UserInfo.image, productId ,reviewMsg, Timestamp.now().toDate(), rate)
            FirebaseReferences.productsRef.document(productId).collection("Reviews").document(
                UserInfo.userID!!).set(review)
            reviewBtnSheetDialog.dismiss()
        }
    }
}