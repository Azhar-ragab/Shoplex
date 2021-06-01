package com.shoplex.shoplex.model.pojo

import com.shoplex.shoplex.model.enumurations.AuthType
import kotlin.collections.ArrayList

data class User(
    var userID: String = "",
    var name: String = "",
    var email: String = "",
    var location: Location = Location(0.0, 0.0),
    var address: String = "",
    var phone: String = "",
    var image: String = "",
    val favouriteList: ArrayList<String> = ArrayList(),
    val cartList: ArrayList<String> = ArrayList(),
    val authType: AuthType = AuthType.Email
) {
}


