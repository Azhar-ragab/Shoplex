package com.shoplex.shoplex.model.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.RvHomeAdcardviewBinding
import com.shoplex.shoplex.model.pojo.Product
import com.shoplex.shoplex.view.activities.ProductDetails

class AdvertisementsAdapter(val advertisements: ArrayList<Product>) :
    RecyclerView.Adapter<AdvertisementsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            RvHomeAdcardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(advertisements[position])

    override fun getItemCount() = advertisements.size

    inner class ProductViewHolder(val binding: RvHomeAdcardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
       val context :Context =binding.root.context

            Glide.with(binding.root.context).load(product.images[0]).into(binding.imgAdvertisement)
            binding.product= product
           // binding.txtAdvertisement.text=product.name
            // binding.tvOffer.text="Offer ${product.discount} %"
            itemView.setOnClickListener{
                var intent: Intent =  Intent(binding.root.context, ProductDetails::class.java )
                intent.putExtra(context.getString(R.string.productId),product.productID)
                binding.root.context.startActivity(intent)
            }
        }
    }
}