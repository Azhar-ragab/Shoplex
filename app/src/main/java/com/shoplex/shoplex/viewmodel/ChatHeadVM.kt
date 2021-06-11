package com.shoplex.shoplex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoplex.shoplex.model.firebase.ChatHeadDBModel
import com.shoplex.shoplex.model.interfaces.INotifyMVP
import com.shoplex.shoplex.model.pojo.ChatHead

class ChatHeadVM: ViewModel, INotifyMVP {

    private val chatHeadDBModel = ChatHeadDBModel(this)
    var chatHead: MutableLiveData<ArrayList<ChatHead>> = MutableLiveData()

    constructor(){

    }

    override fun ongetChatHead(chatHeads: ArrayList<ChatHead>) {
        this.chatHead.value = chatHeads
    }
    fun getChatHead(){
        chatHeadDBModel.getChatHead()
    }

}