package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentFavoritesBinding
import com.shoplex.shoplex.model.adapter.FavouriteAdapter
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.ProductFavourite
import com.shoplex.shoplex.model.interfaces.FavouriteCartListener
import com.shoplex.shoplex.room.viewmodel.FavouriteFactoryModel
import com.shoplex.shoplex.room.viewmodel.FavouriteViewModel

class FavoritesFragment : Fragment(), FavouriteCartListener {
    private lateinit var binding: FragmentFavoritesBinding
    lateinit var favouriteViewModel: FavouriteViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        favouriteViewModel = ViewModelProvider(this, FavouriteFactoryModel(requireContext())).get(FavouriteViewModel::class.java)
        if (UserInfo.userID != null) {
            getAllFavoriteProducts()
        } else {
            Toast.makeText(context, getString(R.string.favoriteproducts), Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    fun getAllFavoriteProducts() {

        val favouriteAdapter = FavouriteAdapter()
        binding.rvFavourite.adapter = favouriteAdapter
        favouriteViewModel.readAllFavourite.observe(viewLifecycleOwner, Observer {
            favouriteAdapter.setData(it)
        })

//        var favouriteProducts = ArrayList<ProductFavourite>()
//        FirebaseReferences.usersRef.document(UserInfo.userID.toString()).get()
//            .addOnSuccessListener { result ->
//                val favouriteList: ArrayList<String> =
//                    result.get("favouriteList") as ArrayList<String>
//                for (productID in favouriteList) {
//                    FirebaseReferences.productsRef.document(productID).get()
//                        .addOnSuccessListener { productResult ->
//                            if (productResult != null) {
//                                val prod = productResult.toObject<ProductFavourite>()
//                                if (prod != null) {
//                                    favouriteProducts.add(prod)
//                                }
//                                if (productID == favouriteList.last()) {
//                                    favouriteAdapter =
//                                        FavouriteAdapter(favouriteProducts,this)
//                                    binding.rvFavourite.adapter = favouriteAdapter
//                                }
//                            }
//                        }
//                }
//            }
    }

    override fun onDeleteFromFavourite(productID: String) {
        favouriteViewModel.deleteFavourite(productID)
    }
}