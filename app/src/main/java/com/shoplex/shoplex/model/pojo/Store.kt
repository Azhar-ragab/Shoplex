package com.shoplex.shoplex.model.pojo

import com.google.android.gms.maps.model.LatLng


data class Store (val storeID:String="", val name:String="", val email:String="", val location: LatLng =LatLng(0.0,0.0), val phone:String="",val address:String=""){
}