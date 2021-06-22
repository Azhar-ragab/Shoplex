package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.DialogAddReviewBinding
import com.shoplex.shoplex.databinding.OrderItemRowBinding
import com.shoplex.shoplex.model.enumurations.OrderStatus
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Order
import com.shoplex.shoplex.model.pojo.Review

class OrderAdapter(var ordersInfo: ArrayList<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            OrderItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) =
        holder.bind(ordersInfo[position])

    override fun getItemCount() = ordersInfo.size

    inner class OrderViewHolder(val binding: OrderItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            if (order.product != null) {
                binding.order = order
                Glide.with(itemView.context).load(order.product!!.images.firstOrNull())
                    .error(R.drawable.product).into(binding.imgProduct)
                if (order.orderStatus == OrderStatus.Current) {
                    binding.tvbutton.text = itemView.context.resources.getString(R.string.cancel)
                    binding.tvbutton.setOnClickListener {
                        FirebaseReferences.ordersRef.document(order.orderID)
                            .update("orderStatus", OrderStatus.Canceled).addOnSuccessListener {
                                Toast.makeText(binding.root.context, "success", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }

                } else {
                    binding.tvbutton.text = binding.root.context.getString(R.string.Review)
                }

                binding.tvbutton.setOnClickListener {
                    showAddReviewDialog(order.productID)
                }
            }
        }

        private fun showAddReviewDialog(productId: String) {
            val binding = DialogAddReviewBinding.inflate(LayoutInflater.from(binding.root.context))
            val reviewBtnSheetDialog = BottomSheetDialog(binding.root.context)
            reviewBtnSheetDialog.setContentView(binding.root)

            FirebaseReferences.productsRef.document(productId).collection("Reviews")
                .document(UserInfo.userID!!).get().addOnSuccessListener {
                    if (it.exists()) {
                        val review: Review = it.toObject()!!
                        binding.rbAddReview.rating = review.rate
                        binding.edReview.setText(review.comment)
                        binding.btnSendReview.text =
                            binding.root.context.getString(R.string.UpdateReview)
                    }
                    reviewBtnSheetDialog.show()
                }

            binding.btnSendReview.setOnClickListener {
                val rate: Float = binding.rbAddReview.rating
                val reviewMsg = binding.edReview.text.toString()
                val review = Review(productId, UserInfo.name, UserInfo.image, reviewMsg, rate)
                FirebaseReferences.productsRef.document(productId).collection("Reviews")
                    .document(UserInfo.userID!!).set(review)
                reviewBtnSheetDialog.dismiss()
            }
        }
    }
}