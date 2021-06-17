package com.shoplex.shoplex.model.pojo

//class Checkout {
//    var deliveryMethod: DeliveryMethod = DeliveryMethod.Door
//    var paymentMethod: PaymentMethod = PaymentMethod.Cash
    // @Exclude @get:Exclude
//    var deliveryLoc: Location? = null
//    var deliveryAddress: String = ""
//    var subTotalPrice: Float = 0F
//    var totalDiscount: Float = 0F
    /*
    var shipping: Int = 0
    var totalPrice: Float = 0F
    */

//    var subTotalPrice: MutableLiveData<Float> = MutableLiveData()
//    var totalDiscount: MutableLiveData<Float> = MutableLiveData()
//    var shipping: MutableLiveData<Int> = MutableLiveData()
//    var totalPrice: MutableLiveData<Float> = MutableLiveData()
//    var coupons: MutableLiveData<Float> = MutableLiveData()
//    val isAllProductsReady: MutableLiveData<Boolean> = MutableLiveData()

//    private lateinit var repo: FavoriteCartRepo
//    private lateinit var lifecycleScope: CoroutineScope
//
//    @Exclude @set:Exclude @get:Exclude
//    var itemNum: Int = 1
//
//    @Exclude @set:Exclude @get:Exclude
//    private var context: Context? = null
//
//    constructor(context: Context){
//        this.context = context
//c
//    }
//
//    constructor(deliveryMethod: DeliveryMethod, paymentMethod: PaymentMethod, deliveryLoc: Location?, deliveryAddress: String, subTotalPrice: Float, itemNum: Int = 1){
//        this.deliveryMethod = deliveryMethod
//        this.paymentMethod = paymentMethod
//        this.deliveryLoc = deliveryLoc
//        this.deliveryAddress = deliveryAddress
//
//        this.itemNum = itemNum
//        //this.deliveryLoc = LatLng(UserInfo.location.latitude, UserInfo.location.longitude)
//    }
//
//    init {
//        this.subTotalPrice.value = 0F
//        this.totalDiscount.value = 0F
//        this.shipping.value = 0
//        this.totalPrice.value = 0F
//        this.coupons.value = 0F
//        this.isAllProductsReady.value = false
//    }
//
//    constructor()
//
//    @Exclude @set:Exclude @get:Exclude
//    private var products: ArrayList<ProductCart> = arrayListOf()
//
//    fun addProduct(productCart: ProductCart){
//        this.products.add(productCart)
//        this.subTotalPrice.value = this.subTotalPrice.value?.plus((productCart.quantity * productCart.newPrice))
//        //this.totalPrice = this.subTotalPrice
//        var discount: Float = 0F
//        if(productCart.specialDiscount != null){
//            discount = if(productCart.specialDiscount!!.discountType == DiscountType.Fixed) {
//                productCart.specialDiscount!!.discount
//            }else{
//                productCart.price * (productCart.specialDiscount!!.discount / 100)
//            }
//
//            this.coupons.value = this.coupons.value?.plus(discount)
//        }else if(productCart.price != productCart.newPrice){
//            discount = (productCart.price - productCart.newPrice)
//        }
//
//        discount = "%.2f".format(discount).toFloat()
//        productCart.discount = discount
//        productCart.newPrice = "%.2f".format(productCart.price - productCart.discount).toFloat()
//
//        addShipping(productCart)
//        // this.shipping += productCart.shipping
//        this.totalDiscount.value = this.totalDiscount.value?.plus((productCart.quantity * discount))
//        this.totalPrice.value = "%.2f".format(subTotalPrice.value?.minus(totalDiscount.value!!)).toFloat()
//    }
//
//    @Exclude
//    fun getAllProducts(): ArrayList<ProductCart>{
//        return products
//    }
//
//    private fun addShipping(productCart: ProductCart){
//        GlobalScope.launch(Dispatchers.IO) {
//            val info: RouteInfo? = LocationManager.getInstance(context!!).getRouteInfo(
//                deliveryLoc!!,
//                productCart.storeLocation
//            )
//
//            var res = "N/A"
//
//            if(info != null){
//                res = info.distance!!
//            }
//            val cost = calcShipping(res)
//
//            totalPrice.postValue(totalPrice.value?.plus(cost))
//            shipping.postValue(shipping.value?.plus(cost))
//            productCart.shipping = cost.toFloat()
//            if(productCart == products.last()){
//                isAllProductsReady.postValue(true)
//            }
//        }
////        repo.storeLocationInfo.observe(context as AppCompatActivity, {
////            if (it != null) {
////                val cost = calcShipping(it.distance!!)
////                this.totalPrice.value = this.totalPrice.value?.plus(cost)
////                this.shipping.value = this.shipping.value?.plus(cost)
////            } else {
////
////            }
////        })
////        this.totalPrice.value = this.totalPrice.value?.plus(10)
////        this.shipping.value = this.shipping.value?.plus(10)
//    }
//
//    fun reAddShipping(){
//        this.totalPrice.postValue(totalPrice.value?.minus(this.shipping.value!!))
//        this.shipping.value = 0
//        this.isAllProductsReady.value = false
//        for(product in products){
//            addShipping(product)
//        }
//    }
//
//    private fun calcShipping(distance: String): Int{
//        return if(distance.contains("km", true)){
//            (5 * distance.split(" ")[0].toFloat()).toInt()
//        }else{
//            15
//        }
//    }
//}