package com.shoplex.shoplex.model.adapter

import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ChatItemRightBinding
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.pojo.Message
import com.shoplex.shoplex.room.viewmodel.MessageViewModel
import com.xwray.groupie.databinding.BindableItem

class RightMessageItem(val message: Message, private val messageVM: MessageViewModel) : BindableItem<ChatItemRightBinding>(){

    override fun bind(binding: ChatItemRightBinding, position: Int) {
        binding.message = message

        if (!message.isRead) {
            FirebaseReferences.chatRef.document(message.chatID).collection("messages")
                .whereEqualTo("messageID", message.messageID).addSnapshotListener { value, error ->
                    if (value == null || error != null || value.documents.isNullOrEmpty())
                        return@addSnapshotListener

                    val updatedMessage = value.documents.first().toObject<Message>()
                    if (updatedMessage != null) {
                        updatedMessage.chatID = message.chatID
                        if (updatedMessage.isSent)
                            messageVM.setSent(updatedMessage.messageID)
                        if (updatedMessage.isRead)
                            messageVM.setRead(updatedMessage.messageID)
                        binding.message = updatedMessage
                    }
                }
        }
    }

    override fun getLayout(): Int {
        return R.layout.chat_item_right
    }
}