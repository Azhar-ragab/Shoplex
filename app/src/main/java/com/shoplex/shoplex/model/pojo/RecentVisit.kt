package com.shoplex.shoplex.model.pojo

import com.google.firebase.Timestamp
import com.shoplex.shoplex.model.extra.UserInfo

data class RecentVisit(
    val ID: String = UserInfo.userID!!,
    val name:String = UserInfo.name,
    val email: String = UserInfo.email,
    val image: String = UserInfo.image!!,
    var address: String = UserInfo.address,
    val phone: String = UserInfo.phone?:"N/A",
    val date: Timestamp = Timestamp.now()
)