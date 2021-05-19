package com.shoplex.shoplex.model.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firestore.v1.StructuredQuery
import com.shoplex.shoplex.Orders
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.OrderItemRowBinding
import com.shoplex.shoplex.model.enumurations.OrderStatus

class OrderAdapter (var ordersInfo: ArrayList<Orders>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            OrderItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) =
        holder.bind(ordersInfo[position])

    override fun getItemCount() = ordersInfo.size

    inner class OrderViewHolder(val binding: OrderItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Orders) {

            /*
            Glide.with(itemView.context).load(order.productImageUrl).into(binding.imgProduct)
            binding.tvProductName.text = order.name
            binding.tvCategory.text = order.category.toString()
            binding.tvPrice.text = order.price.toString()
            binding.tvStatus.text = order.orderStatus.toString()
            if(order.orderStatus==OrderStatus.CURRENT){
            binding.tvbutton.text= itemView.getContext().getResources().getString(R.string.cancel)


            }
            else{
                binding.tvbutton.text=itemView.getContext().getResources().getString(R.string.reOrder)

            }
            */
            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "Hello", Toast.LENGTH_SHORT).show()
            }
        }
    }
}