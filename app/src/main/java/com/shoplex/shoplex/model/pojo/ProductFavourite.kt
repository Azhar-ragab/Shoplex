package com.shoplex.shoplex.model.pojo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shoplex.shoplex.Product
import kotlinx.android.parcel.Parcelize

//@Parcelize
//@Entity(tableName = "Favourite")
//data class ProductFavourite(
//    var product: Product= Product(),
//    @PrimaryKey(autoGenerate = true)
//    var id: Int = 0
//
//
//) : Product(),Parcelable {
//    constructor(
//        product: Product
//    ) : this() {
//
//        this.name = product.name
//        this.price = product.price
//        this.newPrice = product.price
//        this.category = product.category
//        this.rate = product.rate
//        this.images = product.images
//
//    }
//}