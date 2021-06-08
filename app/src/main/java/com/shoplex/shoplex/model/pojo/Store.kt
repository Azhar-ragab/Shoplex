package com.shoplex.shoplex.model.pojo

data class Store (val storeID:String="", val name:String="", val email:String="", val locations: ArrayList<Location>? = ArrayList(), val phone:String="", val addresses:ArrayList<String> = ArrayList()){
}