package com.shoplex.shoplex.model.extra

import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.shoplex.shoplex.model.pojo.Location
import com.shoplex.shoplex.model.pojo.NotificationToken
import java.util.*
import kotlin.collections.ArrayList

object UserInfo {
    var userID: String? = null
    var name: String = ""
    var email: String = ""
    var image: String? = null
    var location: Location = Location(0.0, 0.0)
    var address: String = ""
    var phone: String? = null
    var favouriteList: ArrayList<String> = arrayListOf()
    var cartList: ArrayList<String> = arrayListOf()

    fun updateTokenID(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            val token = task.result
            val notificationToken = NotificationToken(this.userID!!, token.toString())
            FirebaseReferences.notificationTokensRef.document(this.userID!!).set(notificationToken)
        })
    }

    fun clear(){
        this.userID = null
        this.name = ""
        this.email = ""
        this.image = null
        this.location = Location(0.0, 0.0)
        this.address = ""
        this.phone = null
        this.favouriteList = arrayListOf()
        this.cartList = arrayListOf()
    }


}