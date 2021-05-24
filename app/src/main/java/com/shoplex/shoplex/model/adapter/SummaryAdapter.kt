package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.OrderItemRowBinding
import com.shoplex.shoplex.databinding.RvSummaryCardviewBinding
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.Products_Home
import com.shoplex.shoplex.model.pojo.Summary_Checkout

class SummaryAdapter(private val summaryCheck: ArrayList<ProductCart>) :
    RecyclerView.Adapter<SummaryAdapter.ViewHolder>() {  // Create a new view, which defines the UI of the list item
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        return ViewHolder(
            RvSummaryCardviewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bind(summaryCheck[position])
    }

    class ViewHolder(val binding: RvSummaryCardviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(productCart: ProductCart){
           // Glide.with(itemView.context).load(productCart.images[0].toString()).into(binding.imgProductSummary)
            binding.tvProductName.text = productCart.name
            binding.tvPriceSimmary.text = productCart.newPrice.toString()
            binding.tvQuantity.text = productCart.quantity.toString()
        }
    }
    override fun getItemCount() = summaryCheck.size

}