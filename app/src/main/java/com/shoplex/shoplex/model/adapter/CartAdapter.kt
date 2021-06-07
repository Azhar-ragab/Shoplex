package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.shoplex.shoplex.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.Product

import com.shoplex.shoplex.databinding.RvCartHomeBinding
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.room.Lisitener

class CartAdapter(
    var carts:ArrayList<ProductCart>,
    var deleteCartClick: Lisitener,
    var updateCartClick: Lisitener
) :
    RecyclerView.Adapter<CartAdapter.ProductViewHolder>() {
    companion object {
        var deleteCart: Lisitener? = null
        var updateCart: Lisitener? = null
    }
//    var carts= emptyList<ProductCart>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            RvCartHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(carts[position])

    override fun getItemCount() = carts.size
    fun setData(product: List<ProductCart>){
        this.carts = product as ArrayList<ProductCart>
        notifyDataSetChanged()
    }
    inner class ProductViewHolder(val binding: RvCartHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductCart) {
            Glide.with(binding.root.context).load(product.images[0]).into(binding.imgCart)
            binding.tvCart.text = product.name
            binding.tvPrice.text = product.price.toString()
            binding.tvCategory.text = product.category
            binding.number.text = product.quantity.toString()
            binding.imgDelete.setOnClickListener {
                deleteCart = deleteCartClick
                if (deleteCart != null) {

                    deleteCart!!.onaddCart(product)
                    notifyDataSetChanged()
                }
            }
            updateCart = updateCartClick
            var quantity = product.quantity
            binding.btnMinus.setOnClickListener {
                if (quantity > 0) {
                    quantity--
                    binding.number.text = quantity.toString()
                    notifyDataSetChanged()
                    Toast.makeText(binding.root.context, quantity.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                if (updateCart != null) {
                    var cart = ProductCart(quantity = quantity)
                    updateCart!!.onUpdateCart(cart)
                }
            }
            binding.btnPlus.setOnClickListener {

                if (quantity >= 0) {
                    quantity++
                    binding.number.text = quantity.toString()
                    notifyDataSetChanged()
                    Toast.makeText(binding.root.context, quantity.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                if (updateCart != null) {
                    var cart = ProductCart(quantity = quantity)
                    updateCart!!.onUpdateCart(cart)
                }
            }
        }
    }
}