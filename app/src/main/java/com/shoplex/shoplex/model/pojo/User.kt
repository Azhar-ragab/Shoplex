package com.shoplex.shoplex.model.pojo

import com.google.type.DateTime

data class User(val UserID:String="",val name:String="",val email:String="",val locations: ArrayList<Location> = ArrayList(),val phone:String="",val password:String="",val imageUrl:String="",val lastVisit:DateTime)
{

}