package com.shoplex.shoplex.room.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.room.data.ShoplexDataBase
import com.shoplex.shoplex.room.repository.CartRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel (app: Application):AndroidViewModel(app){
    private val readAllCart : LiveData<List<ProductCart>>
    private val cartRepo : CartRepo

    init {
        val cartDao = ShoplexDataBase.getDatabase(app).shoplexDao()
        cartRepo = CartRepo(cartDao)
        readAllCart = cartRepo.readCart
    }

    fun addCart(cart: ProductCart){
        viewModelScope.launch(Dispatchers.IO) {
            cartRepo.addCart(cart)
        }
    }
}