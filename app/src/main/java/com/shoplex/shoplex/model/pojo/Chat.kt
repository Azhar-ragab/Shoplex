package com.shoplex.shoplex.model.pojo

import com.google.firebase.firestore.Exclude

data class Chat(var chatID : String = "", val userID: String = "",val userName:String="", val storeID: String = "", var productIDs: ArrayList<String> = arrayListOf(String()),  val unreadCustomerMessages: Int = 0, @Exclude @set:Exclude var unreadStoreMessages: Int = 0)