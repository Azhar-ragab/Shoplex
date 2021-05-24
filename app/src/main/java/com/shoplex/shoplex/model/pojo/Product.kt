package com.shoplex.shoplex


import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.Exclude
import com.shoplex.shoplex.model.enumurations.Category
import com.shoplex.shoplex.model.enumurations.Premium
import com.shoplex.shoplex.model.pojo.Properties
import java.util.*
import kotlin.collections.ArrayList


open class Product : Parcelable{
    var productID : String = UUID.randomUUID().toString()
    var storeID : String = ""
    var storeName : String = ""
    var deliveryLoc: LatLng? = null
    var name : String = ""
    var description: String = ""
    var price : Float = 10F
    var newPrice : Float = 10F
    var discount : Int = 0
    var category : String = ""
    var productNumber : Int = 0
    var subCategory : String = ""
    var rate : Float? = null
    var premium : Premium? = null
    var premiumDays: Int = 0
    var properties: ArrayList<Property> = arrayListOf()
    var date: Date? = null

    var images : ArrayList<String?> = arrayListOf()

    @Exclude @set:Exclude @get:Exclude
    var imagesListURI : ArrayList<Uri> = arrayListOf()

    @Exclude @set:Exclude @get:Exclude
    var imageSlideList : ArrayList<SlideModel> = arrayListOf()

 constructor()

    constructor(
        name: String,
        price: Float,
        category: String,
        deliveryLoc: LatLng,
        productImageUrl: String
    ) {
        this.name = name
        this.price = price
        this.category = category
        this.deliveryLoc = deliveryLoc
        if (images.size > 0){
            this.images[0] = productImageUrl
        }

    }

    constructor(
        name: String,
        newPrice: Float,
        oldPrice: Float,
        sold: String,
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
        this.productNumber = productNumber
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