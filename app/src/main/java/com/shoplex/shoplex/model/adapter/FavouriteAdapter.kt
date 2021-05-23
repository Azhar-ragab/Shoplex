package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.shoplex.shoplex.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.databinding.FavouriteItemRowBinding

class FavouriteAdapter(val favourites: ArrayList<Product>) :
    RecyclerView.Adapter<FavouriteAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            FavouriteItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(favourites[position])

    override fun getItemCount() = favourites.size

    inner class ProductViewHolder(val binding: FavouriteItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            Glide.with(binding.root.context).load(product.images[0]).into(binding.imgProduct)
            binding.tvProductName.text=product.name
            binding.tvPrice.text=product.newPrice.toString()
            binding.tvReview.text=product.rate.toString()
        }
    }
}