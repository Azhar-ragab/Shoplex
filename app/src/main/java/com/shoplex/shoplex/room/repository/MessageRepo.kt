package com.shoplex.shoplex.room.repository

import androidx.lifecycle.LiveData
import com.shoplex.shoplex.model.pojo.Message
import com.shoplex.shoplex.room.data.ShopLexDao

class MessageRepo(private val messageDao: ShopLexDao, val chatID : String) {

    val readAllMessage : LiveData<List<Message>> = messageDao.readAllMessage(chatID)

    suspend fun addMessage(rightMessage : Message){
        messageDao.addMessage(rightMessage)
    }

    fun setSent(messageID: String){
        messageDao.setSent(messageID)
    }

    fun setReadMessage(messageID: String){
        messageDao.setReadMessage(messageID)
    }
}