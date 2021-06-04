package com.shoplex.shoplex.room.repository

import androidx.lifecycle.LiveData
import com.shoplex.shoplex.model.pojo.LastOrder
import com.shoplex.shoplex.room.data.LastOrderDoa


class LastOrderRepo(private val lastOrderDoa: LastOrderDoa) {

    val readAllLastOrder: LiveData<List<LastOrder>> = lastOrderDoa.readAllLastOrder()

    suspend fun addLastOrder(lastOrder: List<LastOrder>){
        lastOrderDoa.addLastOrder(lastOrder)
    }

}