package com.shoplex.shoplex.model.interfaces

import com.shoplex.shoplex.Product

interface INotifyMVP {
    fun onAllProductsReady(products: ArrayList<Product>){}
}