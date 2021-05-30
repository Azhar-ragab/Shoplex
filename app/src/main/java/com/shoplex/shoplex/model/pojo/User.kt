package com.shoplex.shoplex.model.pojo

import java.util.*
import kotlin.collections.ArrayList

data class User(
    var userID: String = "",
    var name: String = "",
    var email: String = "",
    val location: Location = Location(0.0, 0.0),
    var address: String = "",
    var phone: String = "",
    val image: String = "",
    val favouriteList: ArrayList<String> = ArrayList(),
    val cartList: ArrayList<String> = ArrayList()
) {
}


