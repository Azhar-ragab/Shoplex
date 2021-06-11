package com.shoplex.shoplex.model.firebase

import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.interfaces.INotifyMVP
import com.shoplex.shoplex.model.pojo.Chat
import com.shoplex.shoplex.model.pojo.ChatHead
import com.shoplex.shoplex.model.pojo.Product

class ChatHeadDBModel(val notifier: INotifyMVP) {

    fun getChatHead() {
        val chatHead = arrayListOf<ChatHead>()
        FirebaseReferences.chatRef.whereEqualTo("userID", UserInfo.userID).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document != null) {
                        val chat: Chat = document.toObject()
                        FirebaseReferences.productsRef
                            .document(chat.productIDs.last()).get()
                            .addOnSuccessListener { productDocument ->
                                if (productDocument != null) {
                                    val product: Product? = productDocument.toObject()
                                    if (product != null) {
                                        chatHead.add(
                                            ChatHead(
                                                chat.productIDs,
                                                product.storeID,
                                                chat.chatID,
                                                product.name,
                                                product.price,
                                                product.images[0],
                                                chat.userID,
                                                chat.userName,
                                                chat.unreadStoreMessages
                                            )
                                        )
                                        if (document.equals(result.last())) {
                                            this.notifier.ongetChatHead(chatHead)
                                        }
                                    }
                                }
                            }
                    }
                }
            }
    }
}