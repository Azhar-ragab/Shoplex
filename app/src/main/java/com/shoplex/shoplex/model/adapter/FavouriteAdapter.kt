package com.shoplex.shoplex.model.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.shoplex.shoplex.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.databinding.FavouriteItemRowBinding
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.ProductFavourite
import com.shoplex.shoplex.model.pojo.User
import com.shoplex.shoplex.room.Lisitener

class FavouriteAdapter( val deleteFavClick:Lisitener) :
    RecyclerView.Adapter<FavouriteAdapter.ProductViewHolder>() {
    companion object {
        var deleteFavourite: Lisitener? = null
    }
    var favourites= emptyList<ProductFavourite>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            FavouriteItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(favourites[position])

    override fun getItemCount() = favourites.size
    fun setData(product: List<ProductFavourite>){
        this.favourites = product as ArrayList<ProductFavourite>
        notifyDataSetChanged()
    }
    inner class ProductViewHolder(val binding: FavouriteItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductFavourite) {
//            Glide.with(binding.root.context).load(product.images[0]).into(binding.imgProduct)
            binding.product=product
         //   binding.tvProductName.text=product.name
         //   binding.tvPrice.text=product.newPrice.toString()
        //    binding.tvReview.text=product.rate.toString()

            binding.imgDelete.setOnClickListener {
               // val user:User= User()
                //   favourites.remove(product)

                FirebaseReferences.usersRef.document(UserInfo.userID.toString()).update(
                    "favouriteList",FieldValue.arrayRemove(product.productID)
                )
               deleteFavourite = deleteFavClick
                if (deleteFavourite != null) {
                    deleteFavourite!!.ondeleteFavourite(product)

                }
                UserInfo.favouriteList.remove(product.productID)
                notifyDataSetChanged()


            }
        }
    }
}