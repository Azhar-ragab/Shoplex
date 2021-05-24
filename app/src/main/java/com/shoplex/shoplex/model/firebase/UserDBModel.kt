package com.shoplex.shoplex.model.firebase

import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.interfaces.INotifyMVP
import com.shoplex.shoplex.model.pojo.User

class UserDBModel(val notifier: INotifyMVP?) {
    fun getUserByMail(userEmail: String) {
        FirebaseReferences.usersRef.whereEqualTo("email", userEmail).get().addOnSuccessListener {
            if (it.count() > 0) {
                val user: User = it.documents[0].toObject()!!
                UserInfo.userID = user.userID
                UserInfo.image = user.image
                UserInfo.name = user.name
                UserInfo.email = user.email
                UserInfo.location = user.location
                UserInfo.address = user.address
                UserInfo.phone = user.phone
                UserInfo.favouriteList = user.favouriteList
                UserInfo.cartList = user.cartList
                notifier?.onUserInfoReady()
            } else {
                notifier?.onOrderFailed()
                UserInfo.clear()
            }
        }.addOnFailureListener {
            notifier?.onOrderFailed()
            UserInfo.clear()
        }
    }
}