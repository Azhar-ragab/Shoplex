package com.shoplex.shoplex.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.model.enumurations.DeliveryMethod
import com.shoplex.shoplex.model.enumurations.DiscountType
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.interfaces.FavouriteCartListener
import com.shoplex.shoplex.model.maps.LocationManager
import com.shoplex.shoplex.model.maps.RouteInfo
import com.shoplex.shoplex.model.pojo.*
import com.shoplex.shoplex.room.data.ShoplexDataBase
import com.shoplex.shoplex.room.repository.FavoriteCartRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class CheckoutVM(val context: Context): ViewModel(), FavouriteCartListener {
    var deliveryMethod: MutableLiveData<DeliveryMethod> = MutableLiveData()
    var paymentMethod: MutableLiveData<PaymentMethod> = MutableLiveData()
    var deliveryLocation: MutableLiveData<Location> = MutableLiveData()
    var deliveryAddress: MutableLiveData<String> = MutableLiveData()
    //var checkout: MutableLiveData<Checkout> = MutableLiveData()
    var subTotalPrice: MutableLiveData<Float> = MutableLiveData()
    var totalDiscount: MutableLiveData<Float> = MutableLiveData()
    var shipping: MutableLiveData<Float> = MutableLiveData()
    var totalPrice: MutableLiveData<Float> = MutableLiveData()
    var coupons: MutableLiveData<Float> = MutableLiveData()
    val isAllProductsReady: MutableLiveData<Boolean> = MutableLiveData()

    private var itemsNum: Int = 1

    private var repo: FavoriteCartRepo
    private var lifecycleScope: CoroutineScope

    private var products: ArrayList<ProductCart> = arrayListOf()
    var productQuantities: ArrayList<ProductQuantity> = arrayListOf()

//    var subTotal: MutableLiveData<Float> = MutableLiveData()
//    var discount: MutableLiveData<Float> = MutableLiveData()
//    var shipping: MutableLiveData<Int> = MutableLiveData()
//    var total: MutableLiveData<Float> = MutableLiveData()

    init {
        lifecycleScope = (context as AppCompatActivity).lifecycleScope
        repo = FavoriteCartRepo(ShoplexDataBase.getDatabase(context).shoplexDao())

        deliveryMethod.value = DeliveryMethod.Door
        paymentMethod.value = PaymentMethod.Cash
        deliveryLocation.value = UserInfo.location
        deliveryAddress.value = UserInfo.address
        subTotalPrice.value = 0F
        totalDiscount.value = 0F
        shipping.value = 0F
        totalPrice.value = 0F
        coupons.value = 0F
        isAllProductsReady.value = false

        //checkout.value = Checkout(context)

//        subTotal.value = 0F
//        discount.value = 0F
//        shipping.value = 0
//        total.value = 0F

        /*
        deliveryLocation.observe(context as AppCompatActivity, {
            checkout.value?.deliveryLoc = deliveryLocation.value
            checkout.value?.deliveryAddress = deliveryAddress.value!!
        })
        */
    }

    fun getAllCartProducts() {
        // var cartProducts = ArrayList<ProductCart>()
        FirebaseReferences.usersRef.document(UserInfo.userID!!)
            .collection("Lists")
            .document("Cart").get().addOnSuccessListener { result ->
                val cartList: ArrayList<String> = result.get("cartList") as ArrayList<String>
                for (productID in cartList) {
                    FirebaseReferences.productsRef.document(productID).get()
                        .addOnSuccessListener { productResult ->
                            if (productResult != null) {
                                val prod = productResult.toObject<ProductCart>()
                                FirebaseReferences.productsRef.document(productID)
                                    .collection("Special Discounts")
                                    .document(UserInfo.userID!!).get().addOnSuccessListener {
                                        var specialDiscount: SpecialDiscount? = null
                                        if (it.exists()) {
                                            specialDiscount = it.toObject()
                                        }
                                        // LocationManager.getInstance(this).getRouteInfo(UserInfo.location, prod.deliveryLoc)
                                        prod?.cartQuantity = productQuantities.find { product -> product.productID == prod?.productID }?.quantity?: 1

                                        val productCart = ProductCart(prod!!, prod.cartQuantity, specialDiscount)
                                        // cartProducts.add()

                                        if(prod.quantity == 0){
                                            Toast.makeText(context, "Product ${prod.name} out of stock", Toast.LENGTH_SHORT).show()
                                            onDeleteFromFavourite(prod.productID)
                                        }else{
                                            if(prod.cartQuantity > prod.quantity){
                                                Toast.makeText(context, "${prod.cartQuantity - prod.quantity} items of product ${prod.name} was solid remains ${prod.quantity}", Toast.LENGTH_SHORT).show()
                                                onUpdateCartQuantity(prod.productID, prod.quantity)
                                                prod.cartQuantity = prod.quantity
                                            }
                                            addProduct(productCart)
                                        }

//                                        subTotal.value = checkout.value?.subTotalPrice
//                                        discount.value = checkout.value?.totalDiscount
                                        //shipping.value = checkout.value?.shipping
                                        //total.value = checkout.value?.totalPrice
                                    }
                            }
                        }
                }
            }
    }

    fun addProduct(productCart: ProductCart){
        this.products.add(productCart)
        this.subTotalPrice.value = this.subTotalPrice.value?.plus((productCart.cartQuantity * productCart.newPrice))
        //this.totalPrice = this.subTotalPrice
        var discount: Float = 0F
        if(productCart.specialDiscount != null){
            discount = if(productCart.specialDiscount!!.discountType == DiscountType.Fixed) {
                productCart.specialDiscount!!.discount
            }else{
                productCart.price * (productCart.specialDiscount!!.discount / 100)
            }

            this.coupons.value = this.coupons.value?.plus(discount)
        }else if(productCart.price != productCart.newPrice){
            discount = (productCart.price - productCart.newPrice)
        }

        discount = "%.2f".format(discount).toFloat()
        productCart.discount = discount
        productCart.newPrice = "%.2f".format(productCart.price - productCart.discount).toFloat()

        addShipping(productCart)
        // this.shipping += productCart.shipping
        this.totalDiscount.value = this.totalDiscount.value?.plus((productCart.cartQuantity * discount))
        this.totalPrice.value = "%.2f".format(subTotalPrice.value?.minus(totalDiscount.value!!)).toFloat()
    }

    fun getAllProducts(): ArrayList<ProductCart>{
        return products
    }

    private fun addShipping(productCart: ProductCart){
        GlobalScope.launch(Dispatchers.IO) {
            val info: RouteInfo? = LocationManager.getInstance(context).getRouteInfo(
                deliveryLocation.value!!,
                productCart.storeLocation
            )

            var res = "N/A"

            if(info != null){
                res = info.distance!!
            }
            val cost = calcShipping(res)

            totalPrice.postValue(totalPrice.value?.plus(cost))
            shipping.postValue(shipping.value?.plus(cost))
            productCart.shipping = cost.toFloat()
            if(productCart == products.last()){
                isAllProductsReady.postValue(true)
            }
        }
//        repo.storeLocationInfo.observe(context as AppCompatActivity, {
//            if (it != null) {
//                val cost = calcShipping(it.distance!!)
//                this.totalPrice.value = this.totalPrice.value?.plus(cost)
//                this.shipping.value = this.shipping.value?.plus(cost)
//            } else {
//
//            }
//        })
//        this.totalPrice.value = this.totalPrice.value?.plus(10)
//        this.shipping.value = this.shipping.value?.plus(10)
    }

    fun reAddShipping(){
        this.totalPrice.postValue(totalPrice.value?.minus(this.shipping.value!!))
        this.shipping.value = 0F
        this.isAllProductsReady.value = false
        for(product in products){
            addShipping(product)
        }
    }

    private fun calcShipping(distance: String): Int{
        return if(distance.contains("km", true)){
            (5 * distance.split(" ")[0].toFloat()).toInt()
        }else{
            15
        }
    }

    override fun onUpdateCartQuantity(productID: String, quantity: Int) {
        lifecycleScope.launch {
            repo.updateCart(productID, quantity)
        }
    }

    override fun onDeleteFromCart(productID: String) {
        super.onDeleteFromCart(productID)
        lifecycleScope.launch {
            repo.deleteCart(productID)
        }
    }
}