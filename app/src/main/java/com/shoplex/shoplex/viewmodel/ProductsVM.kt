package com.shoplex.shoplex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoplex.shoplex.model.enumurations.Category
import com.shoplex.shoplex.model.firebase.ProductsDBModel
import com.shoplex.shoplex.model.interfaces.INotifyMVP
import com.shoplex.shoplex.model.pojo.*

class ProductsVM: ViewModel, INotifyMVP {
    var products: MutableLiveData<ArrayList<Product>> = MutableLiveData()
    var advertisments: MutableLiveData<ArrayList<Product>> = MutableLiveData()
    private var productsDBModel = ProductsDBModel(this)
    var reviews: MutableLiveData<ArrayList<Review>> = MutableLiveData()
    val reviewStatistics: MutableLiveData<ReviewStatistics> = MutableLiveData()

    constructor() {
        products.value = arrayListOf()
        reviewStatistics.value = ReviewStatistics()
    }

    fun getAllProducts(category: Category, filter: Filter, sort: Sort? = null) {
        productsDBModel.getAllProducts(category, filter, sort)
    }

    fun getProductById(productId: String) {
        productsDBModel.getProductById(productId)
    }

    fun getCategories(): Array<String> {
        return Category.values().map {
            it.toString().split("_").joinToString(" ")
        }.toTypedArray()
    }

    fun getAllPremiums() {
        productsDBModel.getAllPremiums()
    }

    fun getReviewByProductId(productId: String) {
        productsDBModel.getReviewByProductId(productId)
        productsDBModel.getReviewsStatistics(productId)
    }

    override fun onAllProductsReady(products: ArrayList<Product>) {
        this.products.value = products
    }

    override fun onAllAdvertismentsReady(products: ArrayList<Product>) {
        this.advertisments.value = products
    }

    override fun onAllReviwsReady(reviews: ArrayList<Review>) {
        this.reviews.value = reviews
    }

    override fun onReviewStatisticsReady(reviewStatistics: ReviewStatistics) {
        this.reviewStatistics.value = reviewStatistics
    }
}