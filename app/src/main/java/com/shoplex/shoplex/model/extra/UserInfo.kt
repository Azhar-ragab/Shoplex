package com.shoplex.shoplex.model.extra

import android.content.Context
import com.facebook.login.LoginManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shoplex.shoplex.model.pojo.Location
import com.shoplex.shoplex.model.pojo.NotificationToken
import java.lang.reflect.Type
import java.util.*

object UserInfo {
    private val SHARED_USER_INFO = "USER_INFO"
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

    fun saveUserInfo(context: Context){
        context.getSharedPreferences(SHARED_USER_INFO, Context.MODE_PRIVATE).edit()
        .putString("userID", userID)
        .putString("name", name)
        .putString("email", email)
        .putString("image", image)
        .putString("location", Gson().toJson(location))
        .putString("address", address)
        .putString("phone", phone)
        .apply()
    }

    fun readUserInfo(context: Context){
        val sharedPref = context.getSharedPreferences(SHARED_USER_INFO,Context.MODE_PRIVATE)
        userID = sharedPref.getString("userID",null)
        if(userID == null)
            return
        name = sharedPref.getString("name","")!!
        email = sharedPref.getString("email","")!!
        image = sharedPref.getString("image","")
        val locationType: Type = object : TypeToken<Location>() {}.type
        location = Gson().fromJson(sharedPref.getString("location", null), locationType)?: Location()
        address = sharedPref.getString("address","")!!
        phone = sharedPref.getString("phone","")
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

    fun logout(){
        clear()
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
    }
}