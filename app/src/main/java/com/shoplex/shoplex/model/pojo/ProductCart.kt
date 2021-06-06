package com.shoplex.shoplex.model.pojo

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.Property
import com.shoplex.shoplex.model.enumurations.Premium
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "Cart")
data class ProductCart(
    var quantity: Int = 1,
    var specialDiscount: SpecialDiscount? = SpecialDiscount(),
    var shipping: Int = 0, var product: Product? = Product(),
    @PrimaryKey
    var id: Int = 0
) : Product() {


    constructor(
        product: Product,
        quantity: Int,
        specialDiscount: SpecialDiscount,
        shipping: Int
    ) : this() {
        this.productID = product.productID
        this.storeID = product.storeID
        this.storeName = product.storeName
        this.name = product.name
        this.description = product.description
        this.price = product.price
        this.newPrice = product.newPrice
        this.discount = product.discount
        this.category = product.category
        this.subCategory = product.subCategory
        this.rate = product.rate
        this.premium = product.premium
        this.premiumDays = product.premiumDays
        this.properties = product.properties
        this.date = product.date
        this.images = product.images
        this.quantity = quantity
        this.specialDiscount = specialDiscount
        this.shipping = shipping
    }

    constructor(parcel: Parcel) : this() {
        quantity = parcel.readInt()
        specialDiscount = parcel.readParcelable(SpecialDiscount::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeInt(quantity)
        parcel.writeParcelable(specialDiscount, 1)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }

}