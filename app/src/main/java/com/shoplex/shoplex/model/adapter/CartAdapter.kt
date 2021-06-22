package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.RvCartHomeBinding
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.interfaces.FavouriteCartListener

class CartAdapter(
    var favouriteCartListener: FavouriteCartListener
) :
    RecyclerView.Adapter<CartAdapter.ProductViewHolder>() {

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
            Glide.with(binding.root.context).load(product.images.firstOrNull()).error(R.drawable.product).into(binding.imgCart)

            binding.product = product
            binding.imgDelete.setOnClickListener {
                favouriteCartListener.onDeleteFromCart(product.productID)
            }

            binding.btnMinus.setOnClickListener {
                if (product.cartQuantity > 1) {
                    product.cartQuantity--
                    favouriteCartListener.onUpdateCartQuantity(
                        product.productID,
                        product.cartQuantity--
                    )
                }
            }
            binding.btnPlus.setOnClickListener {
                if (product.cartQuantity < product.quantity) {
                    product.cartQuantity++
                    favouriteCartListener.onUpdateCartQuantity(
                        product.productID,
                        product.cartQuantity
                    )
                } else {
                    Toast.makeText(binding.root.context, "Max Quantity", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}