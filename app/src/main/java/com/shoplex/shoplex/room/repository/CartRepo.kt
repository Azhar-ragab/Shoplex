package com.shoplex.shoplex.room.repository

import androidx.lifecycle.LiveData
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.room.data.ShopLexDao

class CartRepo (private val shopLexDao: ShopLexDao) {

    val readCart: LiveData<List<ProductCart>> = shopLexDao.readCart()

    suspend fun addCart(cart: ProductCart) {
        shopLexDao.addCart(cart)
    }

    suspend fun deleteCart(productID: String) {
        shopLexDao.deleteCart(productID)
    }

    suspend fun updateCart(productID: String, quantity: Int) {
        shopLexDao.updateCart(productID, quantity)
    }
}