package com.shoplex.shoplex.model.pojo

import androidx.room.Entity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

import java.util.*

@Entity(tableName = "messages", primaryKeys = ["messageID", "chatID"])
data class Message(var messageID : String = Timestamp.now().toDate().time.toString(), val messageDate: Date = Timestamp.now().toDate(), val toId : String = "0", val message: String = "", @field:JvmField val isSent: Boolean = false, @field:JvmField val isRead: Boolean = false, @Exclude @set:Exclude @get:Exclude var chatID:String = "")