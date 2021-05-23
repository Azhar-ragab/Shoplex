package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.shoplex.shoplex.R
import androidx.recyclerview.widget.RecyclerView
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
        fun bind(item: Product) {
            // Your custom view code here
        }
    }
}