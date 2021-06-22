package com.shoplex.shoplex.room.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.shoplex.shoplex.model.pojo.ProductFavourite
import com.shoplex.shoplex.room.data.ShopLexDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class FavouriteRepo(private val shopLexDao: ShopLexDao) {

    val readFavourite: LiveData<List<ProductFavourite>> = shopLexDao.readFavourite()
    var productID = MutableStateFlow("")

    val searchFavourite = productID.flatMapLatest {
        shopLexDao.searchFav(it)
    }.asLiveData()

    suspend fun addFavourite(favourite: ProductFavourite) {
        shopLexDao.addFavourite(favourite)
    }

    suspend fun deleteFavourite(productId: String) {
        shopLexDao.deleteFavourite(productId)
    }

    fun searchFavourite(productId: String) {
        shopLexDao.searchFav(productId)
    }
}