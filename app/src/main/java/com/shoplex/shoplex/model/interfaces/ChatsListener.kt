package com.shoplex.shoplex.model.interfaces

import com.shoplex.shoplex.model.pojo.ChatHead

interface ChatsListener {
    fun onChatHeadsReady(chatHeads: ArrayList<ChatHead>){}
    fun onChatHeadChanged(position: Int){}
}