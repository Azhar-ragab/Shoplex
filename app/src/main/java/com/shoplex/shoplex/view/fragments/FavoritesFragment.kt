package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shoplex.shoplex.databinding.FragmentFavoritesBinding
import com.shoplex.shoplex.model.adapter.FavouriteAdapter
import com.shoplex.shoplex.model.interfaces.FavouriteCartListener
import com.shoplex.shoplex.room.viewmodel.FavouriteFactoryModel
import com.shoplex.shoplex.room.viewmodel.FavouriteViewModel

class FavoritesFragment : Fragment(), FavouriteCartListener {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favouriteViewModel: FavouriteViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        favouriteViewModel = ViewModelProvider(
            this,
            FavouriteFactoryModel(requireContext())
        ).get(FavouriteViewModel::class.java)

        getAllFavoriteProducts()

        return binding.root
    }

    private fun getAllFavoriteProducts() {
        val favouriteAdapter = FavouriteAdapter()
        binding.rvFavourite.adapter = favouriteAdapter
        favouriteViewModel.readAllFavourite.observe(viewLifecycleOwner, {
            favouriteAdapter.setData(it)
        })
    }

    override fun onDeleteFromFavourite(productID: String) {
        favouriteViewModel.deleteFavourite(productID)
    }
}