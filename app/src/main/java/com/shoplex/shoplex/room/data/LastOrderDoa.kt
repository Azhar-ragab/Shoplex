package com.shoplex.shoplex.room.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shoplex.shoplex.model.pojo.Order
/*
@Dao
interface LastOrderDoa {

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun addLastOrder(lastOrder:Order)

        @Query("SELECT * FROM lastOrder")
        fun readAllLastOrder(): LiveData<List<Order>>
}*/