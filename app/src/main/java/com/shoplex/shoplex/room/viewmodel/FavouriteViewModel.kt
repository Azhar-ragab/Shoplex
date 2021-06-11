package com.shoplex.shoplex.room.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoplex.shoplex.model.pojo.ProductFavourite
import com.shoplex.shoplex.room.data.ShoplexDataBase
import com.shoplex.shoplex.room.repository.FavouriteRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(context: Context) : ViewModel() {
    val readAllFavourite: LiveData<List<ProductFavourite>>
    var searchFavourite: LiveData<ProductFavourite>
    private val favouriteRepo:FavouriteRepo

    init {
        val favouriteDao = ShoplexDataBase.getDatabase(context).shoplexDao()
        favouriteRepo = FavouriteRepo(favouriteDao)
        readAllFavourite = favouriteRepo.readFavourite
        searchFavourite = favouriteRepo.searchFavourite
    }

    fun addFavourite(favourite: ProductFavourite) {
        viewModelScope.launch(Dispatchers.IO) {
            favouriteRepo.addFavourite(favourite)
        }
    }

    fun deleteFavourite(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            favouriteRepo.deleteFavourite(productId)
        }
    }

    fun searchFavourite(productId:String){
        viewModelScope.launch(Dispatchers.IO) {
            favouriteRepo.productID.value = productId
        }
    }
}