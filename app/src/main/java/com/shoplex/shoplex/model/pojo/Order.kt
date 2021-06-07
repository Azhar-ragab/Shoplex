package com.shoplex.shoplex.model.pojo

import androidx.room.Entity
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.Exclude
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.model.enumurations.DeliveryMethod
import com.shoplex.shoplex.model.enumurations.OrderStatus
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.model.extra.UserInfo
import java.util.*
@Entity (tableName = "lastOrder")
class Order: Checkout {
    var orderID: String = UUID.randomUUID().toString()
    var productID: String = ""
    var userID: String = ""
    var storeID: String = ""
    var storeName: String = ""
    var orderStatus: OrderStatus = OrderStatus.Current
    var quantity: Int = 1
    var specialDiscount: SpecialDiscount? = null
    @Exclude @set:Exclude @get:Exclude
    var product:Product? = null
    private constructor(
        deliveryMethod: DeliveryMethod,
        paymentMethod: PaymentMethod,
        deliveryLoc: LatLng?,
        deliveryAddress: String,
        subTotalPrice: Float,
        shipping: Int,
        itemNum: Int = 1
    ) :
            super(deliveryMethod, paymentMethod, deliveryLoc, deliveryAddress, subTotalPrice, shipping, itemNum) {
    }

    constructor(product: ProductCart, checkout: Checkout, orderStatus: OrderStatus) :
            this(
                checkout.deliveryMethod,
                checkout.paymentMethod,
                checkout.deliveryLoc,
                checkout.deliveryAddress,
                checkout.subTotalPrice,
                checkout.shipping,
                checkout.itemNum
            ) {
        this.productID = product.productID
        this.userID = UserInfo.userID!!
        this.orderStatus = orderStatus
        this.quantity = product.quantity
        this.specialDiscount = product.specialDiscount
        this.totalDiscount = if (this.specialDiscount != null)
            this.specialDiscount!!.discount
        else
            product.price - product.newPrice
        this.totalPrice = this.subTotalPrice + this.shipping - this.totalDiscount
    }
    constructor() : super()

}