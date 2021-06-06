package com.shoplex.shoplex.model.pojo

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.Exclude
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.model.enumurations.DeliveryMethod
import com.shoplex.shoplex.model.enumurations.DiscountType
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.maps.LocationManager

open class Checkout {
    var deliveryMethod: DeliveryMethod = DeliveryMethod.Door
    var paymentMethod: PaymentMethod = PaymentMethod.Cash
    // @Exclude @get:Exclude
    var deliveryLoc: Location? = null
    var deliveryAddress: String = ""
    var subTotalPrice: Float = 0F
    var totalDiscount: Float = 0F
    var shipping: Int = 0
    var totalPrice: Float = 0F
    @Exclude @set:Exclude @get:Exclude
    var itemNum: Int = 1

    constructor(){}

    constructor(deliveryMethod: DeliveryMethod, paymentMethod: PaymentMethod, deliveryLoc: Location?, deliveryAddress: String, subTotalPrice: Float, shipping: Int, itemNum: Int = 1){
        this.deliveryMethod = deliveryMethod
        this.paymentMethod = paymentMethod
        this.deliveryLoc = deliveryLoc
        this.deliveryAddress = deliveryAddress
        this.subTotalPrice = subTotalPrice
        this.shipping = shipping
        this.totalPrice = subTotalPrice + shipping
        this.itemNum = itemNum
        //this.deliveryLoc = LatLng(UserInfo.location.latitude, UserInfo.location.longitude)
    }

    @Exclude @set:Exclude @get:Exclude
    private var products: ArrayList<ProductCart> = arrayListOf()

    fun addProduct(productCart: ProductCart){
        this.products.add(productCart)
        this.subTotalPrice += (productCart.quantity * productCart.price)
        //this.totalPrice = this.subTotalPrice
        var discount: Float = 0F
        if(productCart.specialDiscount != null){
            discount = if(productCart.specialDiscount!!.discountType == DiscountType.Fixed) {
                productCart.specialDiscount!!.discount
            }else{
                productCart.price * (productCart.specialDiscount!!.discount / 100)
            }
        }else if(productCart.price != productCart.newPrice){
            discount = (productCart.price - productCart.newPrice)
        }

        this.shipping += productCart.shipping
        this.totalDiscount += (productCart.quantity * discount)
        this.totalPrice = subTotalPrice - totalDiscount + shipping
    }

    @Exclude
    fun getAllProducts(): ArrayList<ProductCart>{
        return products
    }
}