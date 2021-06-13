package com.shoplex.shoplex.room.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.ProductFavourite
import com.shoplex.shoplex.model.pojo.StoreLocationInfo
import com.shoplex.shoplex.room.data.ShopLexDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class FavoriteCartRepo (private val shopLexDao: ShopLexDao) {

    var productID = MutableStateFlow("")
    var storeIfo = MutableStateFlow(StoreLocationInfo())

    // Favorite
    val favoriteProducts: LiveData<List<ProductFavourite>> = shopLexDao.readFavourite()

    val searchFavouriteByID = productID.flatMapLatest {
        shopLexDao.searchFav(it)
    }.asLiveData()

    val storeLocationInfo = storeIfo.flatMapLatest {
        shopLexDao.getLocation(it.storeID, it.location)
    }.asLiveData()

    suspend fun addFavourite(favourite: ProductFavourite) {
        shopLexDao.addFavourite(favourite)
    }

    suspend fun deleteFavourite(productID: String) {
        shopLexDao.deleteFavourite(productID)
    }

    // Cart
    val cartProducts: LiveData<List<ProductCart>> = shopLexDao.readCart()

    val searchCartByID = productID.flatMapLatest {
        shopLexDao.searchCart(it)
    }.asLiveData()

    suspend fun addCart(cart: ProductCart){
        shopLexDao.addCart(cart)
    }

    suspend fun deleteCart(productID: String){
        shopLexDao.deleteCart(productID)
    }

    suspend fun updateCart(productID: String, quantity: Int){
        shopLexDao.updateCart(productID, quantity)
    }

    suspend fun addNewLocation(location: StoreLocationInfo){
        shopLexDao.addLocation(location)
    }
}