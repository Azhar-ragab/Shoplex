package com.shoplex.shoplex.model.pojo

import com.google.firebase.Timestamp
import java.util.*
import kotlin.collections.ArrayList

//class ChatHead {
//
//    var productID : Int = 0
//    var storeID : Int = 0
//    var chatID : Int = 0
//    var productName : String  = ""
//    var discountInfo : String = ""
//    var price : Double  = 0.0
//    var productImageUrl : String = ""
//    var userName : String = ""
//    var numOfMessage : Int = 0
//    var storeImage: String =""
//    constructor(
//        productName: String,
//        price: Double,
//        productImageUrl: String,
//        userName: String,
//        numOfMessage: Int
//    ) {
//        this.productName = productName
//        this.price = price
//        this.productImageUrl = productImageUrl
//        this.userName = userName
//        this.numOfMessage = numOfMessage
//    }
//
//    constructor()
//    constructor(storeImage: String) {
//        this.storeImage = storeImage
//    }
//
//    fun getChatHeadsInfo() : ArrayList<ChatHead>{
//        return arrayListOf(ChatHead())
//    }
//
//}
data class ChatHead(
    var productsIDs: ArrayList<String> = arrayListOf(),
    var storeId: String =" ",
    val chatId: String="",
    var productName:String="",
    var price: Float=0.0F,
    var productImageURL: String? ="",
    val userID : String = " ",
    val userName: String="",
    var numOfMessage: Int=0,
    val date : Date = Timestamp.now().toDate()) {

}