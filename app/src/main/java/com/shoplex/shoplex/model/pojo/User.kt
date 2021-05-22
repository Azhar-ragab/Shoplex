package com.shoplex.shoplex.model.pojo

import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.collections.ArrayList

class User {

    var clientID : String = UUID.randomUUID().toString()
    var name : String = ""
    var email : String = ""
    var image : String = ""
    var locations : ArrayList<LatLng> = arrayListOf()
    var addresses : ArrayList<String> = arrayListOf()
    var phone : String? = null
    var date: Date? = null

    fun getClientInfo(clientID : Int) : User{

        return User()
    }
}