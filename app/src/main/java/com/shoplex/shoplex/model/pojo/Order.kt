package com.shoplex.shoplex.model.pojo

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.shoplex.shoplex.model.enumurations.DeliveryMethod
import com.shoplex.shoplex.model.enumurations.OrderStatus
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.model.extra.UserInfo
import java.util.*

class Order {
    val orderID: String = UUID.randomUUID().toString()
    var productID: String = ""
    var userID: String = ""
    var storeID: String = ""
    var storeName: String = ""
    var orderStatus: OrderStatus = OrderStatus.Current
    var quantity: Int = 1
    var specialDiscount: SpecialDiscount? = null

    var productPrice: Float = 0.0F
    var subTotalPrice: Float = 0F
    var totalDiscount: Float = 0F
    var shipping: Float = 0F
    var totalPrice: Float = 0F

    @Exclude @set:Exclude @get:Exclude
    var product: Product? = null

    var deliveryMethod: String = DeliveryMethod.Door.name
    var paymentMethod: String = PaymentMethod.Cash.name
    var deliveryLoc: Location? = null
    var deliveryAddress: String = ""

    constructor(product: ProductCart)
    {
        this.productID = product.productID
        this.userID = UserInfo.userID!!
        this.storeID = product.storeID
        this.storeName = product.storeName
        this.orderStatus = orderStatus
        this.quantity = product.cartQuantity
        this.specialDiscount = product.specialDiscount

        this.productPrice = product.price
        this.subTotalPrice = "%.2f".format(product.price * product.cartQuantity).toFloat()
        this.totalDiscount = "%.2f".format(product.discount * product.cartQuantity).toFloat()
        this.shipping = product.shipping
        this.totalPrice = this.subTotalPrice + this.shipping - this.totalDiscount

        /*
        this.subTotalPrice = (product.price * product.quantity)
        this.totalDiscount = if (this.specialDiscount != null) {
            this.specialDiscount!!.discount
        }
        else {
            this.specialDiscount = SpecialDiscount(product.discount.toFloat(), DiscountType.Percentage)
            product.price - product.newPrice
        }
        this.totalPrice.value = this.subTotalPrice.value!! + this.shipping.value!! - this.totalDiscount.value!!
        */
        // this.deliveryAddress = checkout.deliveryAddress
        //this.deliveryLoc = checkout.deliveryLoc
    }
    constructor()
}