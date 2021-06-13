package com.shoplex.shoplex.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FieldValue
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FavouriteItemRowBinding
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.ProductFavourite
import com.shoplex.shoplex.model.interfaces.FavouriteCartListener
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.room.data.ShoplexDataBase
import com.shoplex.shoplex.room.repository.FavoriteCartRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FavouriteAdapter :
    RecyclerView.Adapter<FavouriteAdapter.ProductViewHolder>() {

    var favourites = emptyList<ProductFavourite>()
    private lateinit var context: Context
    private lateinit var repo: FavoriteCartRepo
    private lateinit var lifecycleScope: CoroutineScope

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        context = parent.context
        lifecycleScope = (context as AppCompatActivity).lifecycleScope
        repo = FavoriteCartRepo(ShoplexDataBase.getDatabase(context).shoplexDao())

        return ProductViewHolder(
            FavouriteItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(favourites[position])

    override fun getItemCount() = favourites.size
    fun setData(product: List<ProductFavourite>) {
        this.favourites = product as ArrayList<ProductFavourite>
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(val binding: FavouriteItemRowBinding) :
        RecyclerView.ViewHolder(binding.root), FavouriteCartListener {
        fun bind(product: ProductFavourite) {
            Glide.with(binding.root.context).load(product.images.firstOrNull()).into(binding.imgProduct)
            binding.product = product
            // binding.tvProductName.text=product.name
            // binding.tvPrice.text=product.newPrice.toString()
            // binding.tvReview.text=product.rate.toString()

            repo.searchCartByID.observe(context as AppCompatActivity, {
                if (it == null) {
                    binding.fabAddProduct.setImageDrawable(context.getDrawable(R.drawable.ic_cart))
                    product.isCart = false
                } else {
                    binding.fabAddProduct.setImageDrawable(context.getDrawable(R.drawable.ic_done))
                    product.isCart = true
                }
            })

            onSearchForFavouriteCart(product.productID)

            binding.imgDelete.setOnClickListener {
                // val user:User= User()
                // favourites.remove(product)

//                FirebaseReferences.usersRef.document(UserInfo.userID.toString()).update(
//                    "favouriteList", FieldValue.arrayRemove(product.productID)
//                )
//                deleteFavourite = deleteFavClick
//                if (deleteFavourite != null) {
//                    deleteFavourite!!.onDeleteFromFavourite(product.productID)
//
//                }
                // UserInfo.favouriteList.remove(product.productID)

                onDeleteFromFavourite(product.productID)

                notifyDataSetChanged()
            }

            binding.fabAddProduct.setOnClickListener {
                if (product.isCart) {
                    onDeleteFromCart(product.productID)
                    binding.fabAddProduct.setImageDrawable(context.getDrawable(R.drawable.ic_cart))
                    product.isCart = false
                } else {
                    onAddToCart(ProductCart(product = product))
                    binding.fabAddProduct.setImageDrawable(context.getDrawable(R.drawable.ic_done))
                    product.isCart = true
                }
            }
        }

        override fun onAddToCart(productCart: ProductCart) {
            super.onAddToCart(productCart)
            productCart.cartQuantity = 1
            lifecycleScope.launch {
                repo.addCart(productCart)
            }
        }

        override fun onDeleteFromCart(productID: String) {
            super.onDeleteFromCart(productID)
            lifecycleScope.launch {
                repo.deleteCart(productID)
            }
        }

        override fun onDeleteFromFavourite(productID: String) {
            super.onDeleteFromFavourite(productID)
            lifecycleScope.launch {
                repo.deleteFavourite(productID)
            }
        }

        override fun onSearchForFavouriteCart(productId: String) {
            repo.productID.value = productId
        }
    }
}