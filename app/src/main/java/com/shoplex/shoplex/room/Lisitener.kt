package com.shoplex.shoplex.room

import com.shoplex.shoplex.model.pojo.ProductCart

interface Lisitener {
    fun onaddCart(productCart:ProductCart){}
    fun ondeleteCart(productCart: ProductCart){}
    fun onUpdateCart(productCart: ProductCart){}
    //favourite
    fun onaddFavourite(productFavourite: Product){}
    fun ondeleteFavourite(productFavourite: Product){}
}