package com.shoplex.shoplex.room.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shoplex.shoplex.model.pojo.Message
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.ProductFavourite

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
    suspend fun addFavourite(favourite: ProductFavourite)

    @Delete
    suspend fun deleteFavourite(productFavourite: ProductFavourite)

    @Query("SELECT * FROM Favourite")
    fun readFavourite(): LiveData<List<ProductFavourite>>

    //message
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMessage(message: Message)

    @Query("SELECT * FROM messages where chatID = :chatID order by messageID")
    fun readAllMessage(chatID : String):LiveData<List<Message>>

    @Query("SELECT * FROM Favourite WHERE productID = :productId LIMIT 1")
    fun searchFav(productId:String): LiveData<ProductFavourite>

}

