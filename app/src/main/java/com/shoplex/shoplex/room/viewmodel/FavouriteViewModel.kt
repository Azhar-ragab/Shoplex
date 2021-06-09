package com.shoplex.shoplex.room.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoplex.shoplex.model.pojo.ProductFavourite
import com.shoplex.shoplex.room.data.ShoplexDataBase
import com.shoplex.shoplex.room.repository.FavouriteRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(context: Context,val productId: String) : ViewModel() {
    val readAllFavourite: LiveData<List<ProductFavourite>>
    val searchFavourite:LiveData<ProductFavourite>
    private val favouriteRepo:FavouriteRepo

    init {
        val favouriteDao = ShoplexDataBase.getDatabase(context).shoplexDao()
        favouriteRepo = FavouriteRepo(favouriteDao,productId)
        readAllFavourite = favouriteRepo.readFavourite
        searchFavourite=favouriteRepo.searchFav
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

    fun searchFavourite(productId:String){
        viewModelScope.launch(Dispatchers.IO) {
            favouriteRepo.searchFavourite(productId)
        }
    }



}