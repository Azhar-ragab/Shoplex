package com.shoplex.shoplex.model.interfaces

import com.shoplex.shoplex.model.pojo.*

interface INotifyMVP {
    fun onAllProductsReady(products: ArrayList<Product>){}
    fun onOrderSuccess(){}
    fun onOrderFailed(){}
    fun onAllAdvertismentsReady(products: ArrayList<Product>){}
    fun onUserInfoReady(){}
    fun onUserInfoFailed(){}
    fun onCurrentOrdersReady(orders:ArrayList<Order>){}
    fun onNewFacebookAccount(){}
    fun onLastOrdersReady(lastOrders:ArrayList<Order>){}
    fun onAllReviwsReady(reviews:ArrayList<Review>){}
    fun onReviewStatisticsReady(reviewStatistics: ReviewStatistics){}
    fun ongetChatHead(chatHeads: ArrayList<ChatHead>){}
}