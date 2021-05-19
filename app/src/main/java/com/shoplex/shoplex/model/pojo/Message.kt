package com.shoplex.shoplex.model.pojo

import com.google.firebase.Timestamp

import java.util.*

data class Message(var messageID : String = UUID.randomUUID().toString(), val messageDate: Date = Timestamp.now().toDate(), val toId : String = "0", val message: String = "") {

}