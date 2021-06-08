package com.shoplex.shoplex.room.repository

import androidx.lifecycle.LiveData
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.model.pojo.ProductFavourite
import com.shoplex.shoplex.room.data.ShoplexDao

class FavouriteRepo(private val shoplexDao: ShoplexDao) {

    val readFavourite: LiveData<List<ProductFavourite>> = shoplexDao.readFavourite()

    suspend fun addFavourite(favourite: ProductFavourite) {
        shoplexDao.addFavourite(favourite)
    }

    suspend fun deleteFavourite(productFavourite: ProductFavourite) {
        shoplexDao.deleteFavourite(productFavourite)
    }
}