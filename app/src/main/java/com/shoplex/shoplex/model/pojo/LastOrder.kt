package com.shoplex.shoplex.model.pojo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.model.enumurations.OrderStatus
import kotlinx.android.parcel.Parcelize
import java.util.*
/*
@Parcelize
@Entity (tableName = "lastOrder")
data class LastOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val orderID: String,
    val productID: String,
    val userID : String,
    val storeId : String,
    val storeName : String,
    val image : String,
    val category : String,
    val price : Float,
    val orderStatus: OrderStatus,
    val quantity: Int,
    val specialDiscount: String
):Parcelable
*/