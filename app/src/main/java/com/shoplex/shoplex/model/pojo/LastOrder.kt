package com.shoplex.shoplex.model.pojo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity (tableName = "lastOrder")
data class LastOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val orderId : String,
    val productId : String,
    val storeId : String,
    val name : String,
    val image : String,
    val category : String,
    val price : Float,
    val status : String
):Parcelable