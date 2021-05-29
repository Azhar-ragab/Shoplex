package com.shoplex.shoplex.model.interfaces

import com.shoplex.shoplex.Product
import com.shoplex.shoplex.Review
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Order
import com.shoplex.shoplex.model.pojo.Store

interface INotifyMVP {
    fun onAllProductsReady(products: ArrayList<Product>){}
    fun onStoreInfoReady(stores: ArrayList<Store>){}
    fun onOrderSuccess(){}
    fun onOrderFailed(){}
    fun onAllAdvertismentsReady(products: ArrayList<Product>){}
    fun onUserInfoReady(){}
    fun onUserInfoFailed(){}
    fun onCurrentOrdersReady(orders:ArrayList<Order>){}
    fun onNewFacebookAccount(){}
    fun onLastOrdersReady(lastOrders:ArrayList<Order>){}
    fun onAllReviwsReady(reviews:ArrayList<Review>){}

}