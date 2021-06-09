package com.shoplex.shoplex.room

import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.ProductFavourite

interface Lisitener {
    fun onaddCart(productCart:ProductCart){}
    fun ondeleteCart(productCart: ProductCart){}
    fun onUpdateCart(productCart: ProductCart){}
    //favourite
    fun onaddFavourite(productFavourite: ProductFavourite){}
    fun ondeleteFavourite(productFavourite: ProductFavourite){}
    fun onSearchFav(productId:String){}
}