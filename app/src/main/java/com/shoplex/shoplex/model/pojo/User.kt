package com.shoplex.shoplex.model.pojo

import com.google.type.Date
import com.google.type.DateTime

data class User(
    val userID: String = "",
    val name: String = "",
    val email: String = "",
    val locations: ArrayList<Location> = ArrayList(),
    val phone: String = "",
    val imageUrl: String = "",
    val lastVisit: Date? =null,
    val favouriteList: ArrayList<String> = ArrayList(),
    val cartList: ArrayList<String> = ArrayList()
) {
}


