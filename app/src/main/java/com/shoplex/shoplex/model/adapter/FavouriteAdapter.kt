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
import com.shoplex.shoplex.model.pojo.User

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

            binding.imgDelete.setOnClickListener {
                val user:User= User()
                favourites.remove(product)
                user.favouriteList.remove(product.productID)
                notifyDataSetChanged()
                FirebaseReferences.usersRef.whereEqualTo("email", Firebase.auth.currentUser.email).get().addOnSuccessListener { result ->
                    for (document in result){
                        if (document.exists()) {
                            val u = document.toObject<User>()
                            val updates = hashMapOf<String, Any>(
                                "favouriteList" to FieldValue.arrayRemove(product.productID)
                            )
                            FirebaseReferences.usersRef.document(u.userID).update(
                                updates
                            )

                        }
                    }
                }
            }
        }
    }
}