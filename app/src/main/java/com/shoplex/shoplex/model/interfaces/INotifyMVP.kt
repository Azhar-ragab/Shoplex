package com.shoplex.shoplex.model.interfaces

import com.shoplex.shoplex.Product
import com.shoplex.shoplex.model.pojo.Store

interface INotifyMVP {
    fun onAllProductsReady(products: ArrayList<Product>){}
    fun onStoreInfoReady(stores: ArrayList<Store>){}
    fun onOrderSuccess(){}
    fun onOrderFailed(){}
}