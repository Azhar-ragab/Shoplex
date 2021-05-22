package com.shoplex.shoplex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.model.enumurations.Category
import com.shoplex.shoplex.model.firebase.ProductsDBModel
import com.shoplex.shoplex.model.interfaces.INotifyMVP

class ProductsVM: ViewModel, INotifyMVP {
    var products: MutableLiveData<ArrayList<Product>> = MutableLiveData()
    var advertisments:MutableLiveData<ArrayList<Product>> = MutableLiveData()
    private var productsDBModel = ProductsDBModel(this)

    constructor(){
        products.value = arrayListOf()
    }

    fun getAllProducts(category: Category) {
        productsDBModel.getAllProducts(category)
    }

    override fun onAllProductsReady(products: ArrayList<Product>) {
        this.products.value = products
    }

    override fun onAllAdvertismentsReady(products: ArrayList<Product>) {
        this.advertisments.value = products
    }
    fun getProductById(productId: String){
        productsDBModel.getProductById(productId)
    }

    fun getCategories(): Array<String>{
        return Category.values().map {
            it.toString().split("_").joinToString(" ")
        }.toTypedArray()
    }
    fun getAllPremiums(){
        productsDBModel.getAllPremiums()
    }

}