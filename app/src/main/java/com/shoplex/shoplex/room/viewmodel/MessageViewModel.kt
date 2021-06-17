package com.shoplex.shoplex.room.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoplex.shoplex.model.pojo.Message
import com.shoplex.shoplex.room.data.ShopLexDataBase
import com.shoplex.shoplex.room.repository.MessageRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageViewModel(context: Context,chatID : String) : ViewModel() {
    var readAllMessage : LiveData<List<Message>> = MutableLiveData()
    private val messageRepo : MessageRepo

    init {
        val messageDao = ShopLexDataBase.getDatabase(context).shoplexDao()
        messageRepo = MessageRepo(messageDao,chatID)
        readAllMessage = messageRepo.readAllMessage
    }

    fun addMessage(rightMessage: Message){
        viewModelScope.launch(Dispatchers.IO) {
            messageRepo.addMessage(rightMessage)
        }
    }
}