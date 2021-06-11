package com.shoplex.shoplex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.model.enumurations.DeliveryMethod
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.interfaces.FavouriteCartListener
import com.shoplex.shoplex.model.pojo.Checkout
import com.shoplex.shoplex.model.pojo.Location
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.SpecialDiscount
import kotlin.random.Random

class CheckoutVM: ViewModel(), FavouriteCartListener {
    var deliveryMethod: MutableLiveData<DeliveryMethod> = MutableLiveData()
    var paymentMethod: MutableLiveData<PaymentMethod> = MutableLiveData()
    var deliveryLocation: MutableLiveData<Location> = MutableLiveData()
    var deliveryAddress: MutableLiveData<String> = MutableLiveData()
    var checkout: MutableLiveData<Checkout> = MutableLiveData()

    var subTotal: MutableLiveData<Float> = MutableLiveData()
    var discount: MutableLiveData<Float> = MutableLiveData()
    var shipping: MutableLiveData<Int> = MutableLiveData()
    var total: MutableLiveData<Float> = MutableLiveData()

    init{
        deliveryMethod.value = DeliveryMethod.Door
        paymentMethod.value = PaymentMethod.Cash
        deliveryLocation.value = UserInfo.location
        deliveryAddress.value = UserInfo.address
        checkout.value = Checkout()

        subTotal.value = 0F
        discount.value = 0F
        shipping.value = 0
        total.value = 0F
    }

    fun getAllCartProducts() {
        // var cartProducts = ArrayList<ProductCart>()
        FirebaseReferences.usersRef.document(UserInfo.userID!!)
            .collection("Lists")
            .document("Cart").get().addOnSuccessListener { result ->
                val cartList: ArrayList<String> = result.get("cartList") as ArrayList<String>
                for (productID in cartList){
                    FirebaseReferences.productsRef.document(productID).get()
                        .addOnSuccessListener { productResult ->
                            if (productResult != null) {
                                val prod = productResult.toObject<ProductCart>()
                                FirebaseReferences.productsRef.document(productID)
                                    .collection("Special Discounts")
                                    .document(UserInfo.userID!!).get().addOnSuccessListener {
                                        var specialDiscount: SpecialDiscount? = null
                                        if(it.exists()){
                                            specialDiscount = it.toObject()
                                        }
                                        // LocationManager.getInstance(this).getRouteInfo(UserInfo.location, prod.deliveryLoc)
                                        prod?.quantity = Random.nextInt(1, 5)
                                        val productCart = ProductCart(prod!!, specialDiscount, 10)
                                        // cartProducts.add()

                                        checkout.value?.addProduct(productCart)

                                        subTotal.value = checkout.value?.subTotalPrice
                                        discount.value = checkout.value?.totalDiscount
                                        shipping.value = checkout.value?.shipping
                                        total.value = checkout.value?.totalPrice
                                    }
                            }
                        }
                }


            }
    }
}