package com.shoplex.shoplex.model.extra

import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.shoplex.shoplex.R
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

    fun saveSharedPreference(context: Context){
        val sharedPreference = context.getSharedPreferences(context.getString(R.string.UserIngo), Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("userID", userID)
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("image", image)
        editor.putFloat("latitude", location.latitude.toFloat())
        editor.putFloat("longitude", location.longitude.toFloat())
        editor.putString("address", address)
        editor.putString("phone", phone)
        editor.apply()
    }

    fun readSharedPreference(context: Context){
        val sharedPref = context.getSharedPreferences(context.getString(R.string.UserIngo),Context.MODE_PRIVATE)
        sharedPref.getString("userID","")
        sharedPref.getString("name","")
        sharedPref.getString("email","")
        sharedPref.getString("image","")
        sharedPref.getFloat("latitude",0.0f)
        sharedPref.getFloat("longitude",0.0f)
        sharedPref.getString("address","")
        sharedPref.getString("phone","")
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