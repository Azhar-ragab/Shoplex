package com.shoplex.shoplex.model.pojo

import com.google.android.gms.maps.model.LatLng

data class User(
    val userID: String = "",
    val name: String = "",
    val email: String = "",
    val location: Location =Location(0.0,0.0),
    var address: String = "",
    val phone: String = "",
    val image: String? = "",
    val favouriteList: ArrayList<String> = ArrayList(),
    val cartList: ArrayList<String> = ArrayList()
) {
}


