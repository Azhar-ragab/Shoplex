package com.shoplex.shoplex.model.interfaces

import com.shoplex.shoplex.model.pojo.*

interface ProductsListener {
    fun onAllProductsReady(products: ArrayList<Product>){}
    fun onOrderSuccess(){}
    fun onOrderFailed(){}
    fun onAllAdvertisementsReady(products: ArrayList<Product>){}
    fun onCurrentOrdersReady(orders:ArrayList<Order>){}
    fun onLastOrdersReady(lastOrders:ArrayList<Order>){}
    fun onAllReviewsReady(reviews:ArrayList<Review>){}
    fun onReviewStatisticsReady(reviewStatistics: ReviewStatistics){}
}