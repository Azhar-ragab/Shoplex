package com.shoplex.shoplex.model.pojo

import com.google.android.gms.maps.model.LatLng
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.Property
import com.shoplex.shoplex.model.enumurations.Premium
import java.util.*
import kotlin.collections.ArrayList

class ProductCart: Product {
    var quantity: Int = 1
    var specialDiscount: SpecialDiscount? = null

    constructor(product: Product) {
        this.productID = product.productID
        this.storeID = product.storeID
        this.storeName = product.storeName
        this.deliveryLoc = product.deliveryLoc
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
    }

    constructor() : super()
}