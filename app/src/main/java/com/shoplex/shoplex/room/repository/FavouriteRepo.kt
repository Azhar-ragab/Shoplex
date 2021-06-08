package com.shoplex.shoplex.room.repository

import androidx.lifecycle.LiveData
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.room.data.ShoplexDao

class FavouriteRepo(private val shoplexDao: ShoplexDao) {

    val readFavourite: LiveData<List<Product>> = shoplexDao.readFavourite()

    suspend fun addFavourite(favourite: Product) {
        shoplexDao.addFavourite(favourite)
    }

    suspend fun deleteFavourite(productFavourite: Product) {
        shoplexDao.deleteFavourite(productFavourite)
    }
}