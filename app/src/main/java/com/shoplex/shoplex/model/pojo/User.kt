package com.shoplex.shoplex.model.pojo

import com.shoplex.shoplex.model.enumurations.AuthType

data class User(
    var userID: String = "",
    var name: String = "",
    var email: String = "",
    var location: Location = Location(0.0, 0.0),
    var address: String = "",
    var phone: String = "",
    var image: String = "",
    val authType: AuthType = AuthType.Email
)


