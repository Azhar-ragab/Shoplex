package com.shoplex.shoplex.model.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.RvHomeProductCardviewBinding
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.pojo.User
import com.shoplex.shoplex.view.activities.ProductDetails

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
            val user:User= User()
            binding.btnFavorite.setOnClickListener {

                user.favouriteList.add(product.productID)
                FirebaseReferences.userRef.whereEqualTo("email",Firebase.auth.currentUser.email).get().addOnSuccessListener { result ->
                    for (document in result){
                        if (document.exists()) {
                            val u = document.toObject<User>()
                            FirebaseReferences.userRef.document(u.userID).update(
                                "favouriteList",
                                FieldValue.arrayUnion(user.favouriteList[0])
                            )
                        }
                    }
                }

            }
            binding.fabAddProduct.setOnClickListener{
                user.cartList.add(product.productID)
                FirebaseReferences.userRef.whereEqualTo("email",Firebase.auth.currentUser.email).get().addOnSuccessListener { result ->
                    for (document in result){
                        if (document.exists()) {
                            val u = document.toObject<User>()
                            FirebaseReferences.userRef.document(u.userID).update(
                                "cartList",
                                FieldValue.arrayUnion(user.cartList[0])
                            )
                        }
                    }
                }
            }
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