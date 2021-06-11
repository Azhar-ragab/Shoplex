package com.shoplex.shoplex.model.pojo

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import com.shoplex.shoplex.Property
import java.util.*

open class Product : Parcelable{
    var productID : String = UUID.randomUUID().toString()
    var storeID : String = ""
    var storeName : String = ""
    var storeLocation: Location = Location()
    var name : String = ""
    var description: String = ""
    var price : Float = 10F
    var newPrice : Float = 10F
    var discount : Int = 0
    var category : String = ""
    // var productNumber : Int = 0
    var subCategory : String = ""
    @Nullable
    var rate : Float? = null
    @Nullable
    var premium : Premium? = null
    // var premiumDays: Int = 0
    var properties: ArrayList<Property> = arrayListOf()
    var date: Date? = null
    var quantity: Int = 1

    var images : ArrayList<String?> = arrayListOf()

    @Ignore
    @Exclude @set:Exclude @get:Exclude
    var imagesListURI : ArrayList<Uri> = arrayListOf()

    @Ignore
    @Exclude @set:Exclude @get:Exclude
    var imageSlideList : ArrayList<SlideModel> = arrayListOf()

    @Ignore
    @Exclude @set:Exclude @get:Exclude
    var isFavourite = false

    @Ignore
    @Exclude @set:Exclude @get:Exclude
    var isCart = false

 constructor()

    constructor(
        name: String,
        price: Float,
        category: String,
        productImageUrl: String
    ) {
        this.name = name
        this.price = price
        this.newPrice = price
        this.category = category
        this.images.add(productImageUrl)
    }

    constructor(
        name: String,
        newPrice: Float,
        oldPrice: Float,
        //sold: String,
        rate: Float,
        productImageUrl: String
    ) {
        this.name = name
        this.newPrice = newPrice
        this.price = oldPrice
        //this.sold = sold
        this.rate = rate
        this.images[0] = productImageUrl
    }

    constructor(parcel: Parcel) : this() {
        name = parcel.readString().toString()
        description = parcel.readString().toString()
        price = parcel.readFloat()
        newPrice = parcel.readFloat()
        discount = parcel.readInt()
        category = parcel.readString().toString()
        subCategory = parcel.readString().toString()
        imagesListURI = parcel.readArrayList(Uri::class.java.classLoader) as ArrayList<Uri>
        quantity = parcel.readInt()
    }

    constructor(
        name: String,
        price: Float,
        category: String,
        productNumber: Int,
        images:String
    ) {
        this.name = name
        this.price = price
        this.category = category
        // this.productNumber = productNumber
        this.images[0] = images
    }

    @Exclude
    fun getImageSlides(): ArrayList<SlideModel>{
        this.imageSlideList.clear()
        for(image in imagesListURI){
            imageSlideList.add(SlideModel(image.toString()))
        }
        return imageSlideList
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeFloat(price)
        parcel.writeFloat(newPrice)
        parcel.writeInt(discount)
        parcel.writeString(category)
        parcel.writeString(subCategory)
        parcel.writeArray(imagesListURI.toArray())
        parcel.writeInt(quantity)
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