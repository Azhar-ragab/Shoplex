package com.shoplex.shoplex.room.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shoplex.shoplex.model.pojo.ProductCart

@Dao
interface ShoplexDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCart(cart: ProductCart)

    @Delete
    suspend fun deleteCart(productCart : ProductCart)
    @Update
    suspend fun updateCart(productCart: ProductCart)

    @Query("SELECT * FROM Cart")
    fun readCart(): LiveData<List<ProductCart>>

    //favourite

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavourite(favourite: Product)

    @Delete
    suspend fun deleteFavourite(productFavourite: Product)

    @Query("SELECT * FROM Favourite")
    fun readFavourite(): LiveData<List<Product>>
}

