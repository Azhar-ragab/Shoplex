package com.shoplex.shoplex.model.pojo

data class Chat(var chatID : String = "", val userID: String = "",val userName:String="", val storeID: String = "", var productIDs: ArrayList<String> = arrayListOf(String())) {

    fun getAllMessages(chatID : Int) : ArrayList<Message>{

        return  arrayListOf(Message())
    }

    fun getMessages(message: Message,chatID : Int) : Boolean{
        return true
    }
}