package com.shoplex.shoplex.model.adapter

import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ChatItemLeftBinding
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.pojo.Message
import com.shoplex.shoplex.room.viewmodel.MessageViewModel
import com.xwray.groupie.databinding.BindableItem

class LeftMessageItem(private val chatID: String, val message: Message, private val messageVM: MessageViewModel) : BindableItem<ChatItemLeftBinding>() {

    override fun bind(binding: ChatItemLeftBinding, position: Int) {
        binding.message = message
        if (!message.isRead) {
            FirebaseReferences.chatRef.document(chatID).collection("messages")
                .document(message.messageID).update("isRead", true)
            messageVM.setRead(message.messageID)
        }
    }

    override fun getLayout(): Int = R.layout.chat_item_left
}