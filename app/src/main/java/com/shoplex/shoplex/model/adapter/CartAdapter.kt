package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shoplex.shoplex.databinding.RvCartHomeBinding
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.room.Lisitener

class CartAdapter(
    var deleteCartClick: Lisitener,
    var updateCartClick: Lisitener
) :
    RecyclerView.Adapter<CartAdapter.ProductViewHolder>() {
    companion object {
        var deleteCart: Lisitener? = null
        var updateCart: Lisitener? = null
    }

    var carts = emptyList<ProductCart>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            RvCartHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(carts[position])

    override fun getItemCount() = carts.size
    fun setData(product: List<ProductCart>) {
        this.carts = product as ArrayList<ProductCart>
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(val binding: RvCartHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductCart) {
            //   Glide.with(binding.root.context).load(product.product!!.images[0]).into(binding.imgCart)
            binding.tvCart.text = product.product!!.name
            binding.tvPrice.text = product.product!!.newPrice.toString()
            binding.tvCategory.text = product.product!!.category
            binding.number.text = product.quantity.toString()
            binding.imgDelete.setOnClickListener {
                deleteCart = deleteCartClick
                if (deleteCart != null) {
                    deleteCart!!.ondeleteCart(product)
                }
            }
            //  var quantity = product.quantity
            // var quant = 1
            updateCart = updateCartClick
            binding.btnMinus.setOnClickListener {
                if (product.quantity > 0) {
                    product.quantity--
                    binding.number.text = product.quantity.toString()
                }
                if (updateCart != null) {
                    updateCart!!.onUpdateCart(product)
                }
            }
            binding.btnPlus.setOnClickListener {
                if (product.quantity < 100) {
                    product.quantity++
                    binding.number.text = product.quantity.toString()
                }
                if (updateCart != null) {
                    updateCart!!.onUpdateCart(product)
                }
            }
        }
    }
}