package com.shoplex.shoplex.room

import com.shoplex.shoplex.Product
import com.shoplex.shoplex.model.pojo.ProductCart

interface Lisitener {
    fun onaddCart(productCart:ProductCart)
}