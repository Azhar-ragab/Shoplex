package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.OrderItemRowBinding
import com.shoplex.shoplex.model.enumurations.OrderStatus
import com.shoplex.shoplex.model.pojo.Order
import com.shoplex.shoplex.model.pojo.ProductCart

class OrderAdapter (var ordersInfo: ArrayList<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            OrderItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) =
        holder.bind(ordersInfo[position])

    override fun getItemCount() = ordersInfo.size

    inner class OrderViewHolder(val binding: OrderItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {

            if (order.product != null) {
                Glide.with(itemView.context).load(order.product?.images!![0])
                    .into(binding.imgProduct)
                binding.tvProductName.text = order.product?.name
                binding.tvCategory.text = order.product?.category.toString()
                binding.tvPrice.text = order.product?.price.toString()
                binding.tvStatus.text = order.orderStatus.toString()
                if (order.orderStatus == OrderStatus.Current) {
                    binding.tvbutton.text =
                        itemView.getContext().getResources().getString(R.string.cancel)

                } else {
                    binding.tvbutton.text =
                        itemView.getContext().getResources().getString(R.string.reOrder)

                }

                itemView.setOnClickListener {
                    Toast.makeText(itemView.context, R.string.Hello, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}