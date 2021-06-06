package com.shoplex.shoplex.room.repository

import androidx.lifecycle.LiveData
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.room.data.ShoplexDao

class CartRepo (private val shoplexDao: ShoplexDao) {

    val readCart: LiveData<List<ProductCart>> = shoplexDao.readCart()

    suspend fun addCart(cart: ProductCart){
        shoplexDao.addCart(cart)
    }
}