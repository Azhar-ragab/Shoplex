package com.shoplex.shoplex.model.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.model.pojo.Product
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.RvHomeProductCardviewBinding
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.ProductFavourite
import com.shoplex.shoplex.model.pojo.User
import com.shoplex.shoplex.room.Lisitener
import com.shoplex.shoplex.room.viewmodel.CartViewModel
import com.shoplex.shoplex.view.activities.ProductDetails

class HomeProductsAdapter(val productsHome: ArrayList<Product>, var addcartClick :Lisitener,var addfavClick:Lisitener) :
    RecyclerView.Adapter<HomeProductsAdapter.ProductViewHolder>() {
   // private lateinit var cartVm : CartViewModel


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
                addfavClick.onSearchFav(product.productID)
//                binding.btnFavorite.setBackgroundResource(R.drawable.ic_favorite_fill)
//                binding.btnFavorite.isClickable = false
//                notifyDataSetChanged()
//                UserInfo.favouriteList.add(product.productID)
//                if (addfavClick!=null){
//                    var favourite=ProductFavourite(product)
//                    addfavClick!!.onaddFavourite(favourite)
//                }
//                FirebaseReferences.usersRef.document(UserInfo.userID.toString()).update(
//                    "favouriteList",
//                    FieldValue.arrayUnion(product.productID)
//                )

            }

            binding.fabAddProduct.setOnClickListener{
                user.cartList.add(product.productID)
                if (addcartClick!=null){
                    var cart=ProductCart(product = product)
                    addcartClick!!.onaddCart(cart)
                }
              //  cartVm=ViewModelProvider(binding.root.).get(CartViewModel::class.java)
                Toast.makeText(binding.root.context,product.productID,Toast.LENGTH_SHORT).show()
                FirebaseReferences.usersRef.whereEqualTo(binding.root.context.getString(R.string.mail),Firebase.auth.currentUser.email).get().addOnSuccessListener { result ->
                    for (document in result){
                        if (document.exists()) {
                            val u = document.toObject<User>()
                            FirebaseReferences.usersRef.document(u.userID).update(
                                binding.root.context.getString(R.string.cartList),
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
            binding.tvSold.text = R.string.twelve.toString()
            binding.tvSpace.text = R.string.Space.toString()
            if(product.images.count() > 0)
                Glide.with(binding.root.context).load(product.images[0]).into(binding.imgProduct)
            itemView.setOnClickListener{
                var intent:Intent=  Intent(binding.root.context,ProductDetails::class.java )
                intent.putExtra(binding.root.context.getString(R.string.productId),product.productID)
                binding.root.context.startActivity(intent)
            }
        }
    }
}