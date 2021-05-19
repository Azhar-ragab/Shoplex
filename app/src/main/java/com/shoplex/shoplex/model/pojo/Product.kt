package com.shoplex.shoplex


import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.Exclude
import com.shoplex.shoplex.model.enumurations.Premium
import com.shoplex.shoplex.model.pojo.Properties



open class Product: Parcelable {
    var productID : Int? = null
    var storeID : Int = 0
    var storeName : String = ""
    open var name : String = ""
    var description: String = ""
    open var price : Float = 0.0F
    var newPrice : Float = 0.0F
    var oldPrice : Float = 0.0F
    var sold:String=""
    var discount : Int = 0
    var premium : Premium? = null
    var premiumDays: Int = 0
    var properties: ArrayList<Properties> = arrayListOf()


    @Exclude @set:Exclude @get:Exclude
    var imagesListURI : ArrayList<Uri> = arrayListOf()

    @Exclude @set:Exclude @get:Exclude
    var imageSlideList : ArrayList<SlideModel> = arrayListOf()
    open var category : String = ""
    var images : ArrayList<Uri> = ArrayList()
    var subCategory : String =""
    var rate : Double = 0.0
    var permium : Premium = Premium.BASIC
    var deliveryLoc: LatLng = LatLng(0.0, 0.0)
    open var productImageUrl : String = ""

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
        this.productImageUrl = productImageUrl
    }

    constructor(
        name: String,
        newPrice: Float,
        oldPrice: Float,
        sold: String,
        rate: Double,
        productImageUrl: String
    ) {
        this.name = name
        this.newPrice = newPrice
        this.oldPrice = oldPrice
        this.sold = sold
        this.rate = rate
        this.productImageUrl = productImageUrl
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