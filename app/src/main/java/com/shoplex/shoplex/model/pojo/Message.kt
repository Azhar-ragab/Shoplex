package com.shoplex.shoplex.model.pojo

import com.google.firebase.Timestamp

import java.util.*

data class Message(var messageID : String = Timestamp.now().toDate().time.toString(), val messageDate: Date = Timestamp.now().toDate(), val toId : String = "0", val message: String = "", @field:JvmField val isSent: Boolean = false, @field:JvmField val isRead: Boolean = false)