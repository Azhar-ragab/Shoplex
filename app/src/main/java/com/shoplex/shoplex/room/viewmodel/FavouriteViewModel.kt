package com.shoplex.shoplex.room.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.model.pojo.ProductFavourite
import com.shoplex.shoplex.room.data.ShoplexDataBase
import com.shoplex.shoplex.room.repository.FavouriteRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(app: Application) : AndroidViewModel(app) {
    val readAllFavourite: LiveData<List<ProductFavourite>>
    private val favouriteRepo:FavouriteRepo

    init {
        val favouriteDao = ShoplexDataBase.getDatabase(app).shoplexDao()
        favouriteRepo = FavouriteRepo(favouriteDao)
        readAllFavourite = favouriteRepo.readFavourite
    }

    fun addFavourite(favourite: ProductFavourite) {
        viewModelScope.launch(Dispatchers.IO) {
            favouriteRepo.addFavourite(favourite)
        }
    }

    fun deleteFavourite(productFavourite: ProductFavourite) {
        viewModelScope.launch(Dispatchers.IO) {
            favouriteRepo.deleteFavourite(productFavourite)
        }
    }



}