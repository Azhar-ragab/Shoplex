package com.shoplex.shoplex.room.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shoplex.shoplex.model.pojo.LastOrder

@Dao
interface LastOrderDoa {

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun addLastOrder(lastOrder: kotlin.collections.List<com.shoplex.shoplex.model.pojo.LastOrder>)

        @Query("SELECT * FROM lastOrder ORDER BY id ASC")
        fun readAllLastOrder(): LiveData<List<LastOrder>>
}