package com.shoplex.shoplex.model.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.shoplex.shoplex.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.databinding.RvHomeProductCardviewBinding
import com.shoplex.shoplex.view.activities.ProductDetails
import kotlin.coroutines.coroutineContext

class HomeProductsAdapter(val productsHome: ArrayList<Product>) :
    RecyclerView.Adapter<HomeProductsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            RvHomeProductCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(productsHome[position])

    override fun getItemCount() = productsHome.size

    inner class ProductViewHolder(val binding: RvHomeProductCardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {

            binding.tvStorename.text = product.storeName
            binding.tvNewPrice.text = product.newPrice.toString()
            binding.tvOldPrice.text = product.price.toString()
            binding.tvProductName.text =product.name
            binding.tvReview.text = product.rate.toString()
            binding.tvSold.text = "12"
            binding.tvSpace.text = "Space"
            if(product.images.count() > 0)
                Glide.with(binding.root.context).load(product.images[0]).into(binding.imgProduct)
            itemView.setOnClickListener{
                var intent:Intent=  Intent(binding.root.context,ProductDetails::class.java )
                intent.putExtra("productId",product.productID)
                binding.root.context.startActivity(intent)
            }
        }
    }
}