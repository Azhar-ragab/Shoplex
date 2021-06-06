package com.shoplex.shoplex.room.repository

import androidx.lifecycle.LiveData
import com.shoplex.shoplex.model.pojo.LastOrder
import com.shoplex.shoplex.room.data.ShoplexDao


class LastOrderRepo(private val shoplexDao: ShoplexDao) {

    val readAllLastOrder: LiveData<List<LastOrder>> = shoplexDao.readAllLastOrder()

    suspend fun addLastOrder(lastOrder: List<LastOrder>){
        shoplexDao.addLastOrder(lastOrder)
    }

}