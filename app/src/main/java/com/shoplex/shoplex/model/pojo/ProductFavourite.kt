package com.shoplex.shoplex.model.pojo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.shoplex.shoplex.Product
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Favourite")
data class ProductFavourite(
    @Exclude
    @set:Exclude
    @get:Exclude
    var product: Product= Product(),
    @Exclude
    @set:Exclude
    @get:Exclude
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


) : Product(),Parcelable {
    init{
        this.productID=product.productID
        this.name = product.name
        this.price = product.price
        this.newPrice = product.price
        this.category = product.category
        this.rate = product.rate
        this.images = product.images

    }
}