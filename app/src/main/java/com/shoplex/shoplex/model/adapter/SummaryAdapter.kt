package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.databinding.RvSummaryCardviewBinding
import com.shoplex.shoplex.model.pojo.ProductCart

class SummaryAdapter(private val summaryCheck: ArrayList<ProductCart>) :
    RecyclerView.Adapter<SummaryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RvSummaryCardviewBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(summaryCheck[position])

    class ViewHolder(val binding: RvSummaryCardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productCart: ProductCart) {
            if (productCart.images.count() > 0)
                Glide.with(itemView.context).load(productCart.images[0].toString())
                    .into(binding.imgProductSummary)
            binding.tvProductName.text = productCart.name
            binding.tvPriceSimmary.text = productCart.newPrice.toString()
            binding.tvQuantity.text = productCart.cartQuantity.toString()
        }
    }

    override fun getItemCount() = summaryCheck.size
}