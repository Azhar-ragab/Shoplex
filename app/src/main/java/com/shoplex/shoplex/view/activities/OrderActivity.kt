package com.shoplex.shoplex.view.activities

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.animation.OvershootInterpolator
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityOrderBinding
import com.shoplex.shoplex.databinding.DialogAddReviewBinding
import com.shoplex.shoplex.model.adapter.OrderAdapter
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Review
import com.shoplex.shoplex.viewmodel.OrdersVM
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit var ordersVM: OrdersVM

    override fun onCreate(savedInstanceState: Bundle?) {
        if (UserInfo.lang != this.resources.configuration.locale.language)
            UserInfo.setLocale(UserInfo.lang, this)
        super.onCreate(savedInstanceState)
        if (UserInfo.userID == null)
            UserInfo.readUserInfo(this)

        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordersVM = ViewModelProvider(this).get(OrdersVM::class.java)
        setSupportActionBar(binding.toolbarorder)
        supportActionBar?.apply {
            title = getString(R.string.orders)
            // setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        if (ordersVM.currentOrders.value == null)
            ordersVM.getCurrentOrders()
        ordersVM.currentOrders.observe(this, { orders ->
            // binding.rvCurrentOrders.adapter = OrderAdapter(orders)
            binding.rvCurrentOrders.adapter =
                ScaleInAnimationAdapter(SlideInBottomAnimationAdapter(OrderAdapter(orders))).apply {
                    setDuration(700)
                    setInterpolator(OvershootInterpolator(2f))
                }
            if (orders.count()>0) {
                binding.noItemCurrent.visibility= View.INVISIBLE
            }
            else{
                binding.noItemCurrent.visibility= View.VISIBLE
            }
            binding.rvCurrentOrders.adapter = OrderAdapter(orders)
        })

        if (ordersVM.lastOrders.value == null)
            ordersVM.getLastOrders()
        ordersVM.lastOrders.observe(this, { lastOrders ->
            //  binding.rvLastOrders.adapter = OrderAdapter(lastOrders)
            binding.rvLastOrders.adapter =
                ScaleInAnimationAdapter(SlideInBottomAnimationAdapter(OrderAdapter(lastOrders))).apply {
                    setDuration(700)
                    setInterpolator(OvershootInterpolator(2f))
                }
            if (lastOrders.count()>0) {
                binding.noItemLast.visibility= View.INVISIBLE
            }
            else{
                binding.noItemLast.visibility= View.VISIBLE
            }
            binding.rvLastOrders.adapter = OrderAdapter(lastOrders)
        })

        val notificationManager = this.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        if (intent.hasExtra("isNotification")) {
            val productId = intent.getStringExtra("productID")
            notificationManager.cancel(100)
            if (productId != null)
                showAddReviewDialog(productId)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()

        return super.onOptionsItemSelected(item)
    }

    private fun showAddReviewDialog(productId: String) {
        val binding = DialogAddReviewBinding.inflate(LayoutInflater.from(binding.root.context))
        val reviewBtnSheetDialog =
            BottomSheetDialog(binding.root.context, R.style.BottomSheetDialogTheme)
        reviewBtnSheetDialog.setContentView(binding.root)

        FirebaseReferences.productsRef.document(productId).collection("Reviews")
            .document(UserInfo.userID!!).get().addOnSuccessListener {
                if (it.exists()) {
                    val review: Review = it.toObject()!!
                    binding.rbAddReview.rating = review.rate
                    binding.edReview.setText(review.comment)
                    binding.btnSendReview.text = getString(R.string.UpdateReview)
                }
                reviewBtnSheetDialog.show()
            }

        binding.btnSendReview.setOnClickListener {
            val rate: Float = binding.rbAddReview.rating
            val reviewMsg = binding.edReview.text.toString()
            val review = Review(
                productId,
                UserInfo.name,
                UserInfo.image, reviewMsg, rate
            )
            FirebaseReferences.productsRef.document(productId).collection("Reviews").document(
                UserInfo.userID!!
            ).set(review)
            reviewBtnSheetDialog.dismiss()
        }
    }
}