package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentFavoritesBinding
import com.shoplex.shoplex.model.adapter.FavouriteAdapter
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.User
import kotlin.collections.ArrayList

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favouriteAdapter: FavouriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        if(UserInfo.userID != null){
            getAllFavoriteProducts()
        }else{
            Toast.makeText(context, getString(R.string.favoriteproducts), Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    fun getAllFavoriteProducts() {
        var favouriteProducts = ArrayList<Product>()
        FirebaseReferences.usersRef.document(UserInfo.userID.toString()).get().addOnSuccessListener { result ->
            val favouriteList: ArrayList<String> = result.get("favouriteList") as ArrayList<String>
            for (productID in favouriteList){
                FirebaseReferences.productsRef.document(productID).get()
                    .addOnSuccessListener { productResult ->
                        if (productResult != null) {
                            val prod = productResult.toObject<Product>()
                            if (prod!=null){
                            favouriteProducts.add(prod)
                                }
                            if (productID == favouriteList.last()) {
                                favouriteAdapter =
                                    FavouriteAdapter(favouriteProducts)
                                binding.rvFavourite.adapter = favouriteAdapter
                            }
                        }
                    }
            }
        }
    }
}