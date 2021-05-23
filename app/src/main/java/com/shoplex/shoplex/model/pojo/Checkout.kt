package com.shoplex.shoplex.model.pojo

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.Exclude
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.model.enumurations.DeliveryMethod
import com.shoplex.shoplex.model.enumurations.PaymentMethod

open class Checkout {
    var deliveryMethod: DeliveryMethod = DeliveryMethod.Door
    var paymentMethod: PaymentMethod = PaymentMethod.Cash
    var deliveryLoc: LatLng? = null
    var subTotalPrice: Float = 0F
    var totalDiscount: Float = 0F
    var shipping: Float = 0F
    var totalPrice: Float = 0F
    var itemNum: Int = 1

    constructor(){}

    constructor(deliveryMethod: DeliveryMethod, paymentMethod: PaymentMethod, deliveryLoc: LatLng?, subTotalPrice: Float, shipping: Float, itemNum: Int = 1){
        this.deliveryMethod = deliveryMethod
        this.paymentMethod = paymentMethod
        this.deliveryLoc = deliveryLoc
        this.subTotalPrice = subTotalPrice
        this.shipping = shipping
        this.totalPrice = subTotalPrice + shipping
        this.itemNum = itemNum
    }

    @Exclude @set:Exclude @get:Exclude
    private var products: ArrayList<ProductCart> = arrayListOf()

    fun addProduct(productCart: ProductCart){
        this.products.add(productCart)
        this.subTotalPrice += (productCart.quantity * productCart.price)
        //this.totalPrice = this.subTotalPrice
        var discount: Float = 0F
        if(productCart.specialDiscount != null){
            discount = productCart.specialDiscount!!.discount
        }else if(productCart.price != productCart.newPrice){
            discount = (productCart.price - productCart.newPrice)
        }

        this.totalDiscount += discount
        this.totalPrice -= discount
    }

    @Exclude
    fun getAllProducts(): ArrayList<ProductCart>{
        return products
    }
}