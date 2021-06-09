package com.shoplex.shoplex.room.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoplex.shoplex.model.pojo.Message
import com.shoplex.shoplex.room.data.ShoplexDataBase
import com.shoplex.shoplex.room.repository.MessageRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageViewModel(context: Context, chatID : String) : ViewModel() {
    val readAllMessage: LiveData<List<Message>>
    private val messageRepo: MessageRepo

    init {
        val messageDao = ShoplexDataBase.getDatabase(context).shoplexDao()
        messageRepo = MessageRepo(messageDao, chatID)
        readAllMessage = messageRepo.readAllMessage
    }

    fun addMessage(rightMessage: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepo.addMessage(rightMessage)
        }
    }
}