package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.databinding.RvCartHomeBinding
import com.shoplex.shoplex.model.pojo.ProductCart

class CartAdapter(val carts: ArrayList<ProductCart>) :
    RecyclerView.Adapter<CartAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            RvCartHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(carts[position])

    override fun getItemCount() = carts.size

    inner class ProductViewHolder(val binding: RvCartHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product : ProductCart) {
            Glide.with(binding.root.context).load(product.images[0]).into(binding.imgCart)
            binding.tvCart.text=product.name
            binding.tvPrice.text=product.price.toString()
            binding.tvCategory.text=product.category
            binding.number.text=product.quantity.toString()
        }
    }
}