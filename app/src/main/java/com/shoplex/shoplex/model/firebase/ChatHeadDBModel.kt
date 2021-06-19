package com.shoplex.shoplex.model.firebase

import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.interfaces.ChatsListener
import com.shoplex.shoplex.model.pojo.Chat
import com.shoplex.shoplex.model.pojo.ChatHead
import com.shoplex.shoplex.model.pojo.Product

class ChatHeadDBModel(private val listener: ChatsListener) {

    fun getChatHeads() {
        val chatHeads = arrayListOf<ChatHead>()
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
                                        val chatHead = ChatHead(
                                            chat.productIDs.last(),
                                            chat.storeID,
                                            chat.chatID,
                                            product.name,
                                            product.price,
                                            product.images.first(),
                                            chat.userID,
                                            chat.storeName,
                                            chat.unreadCustomerMessages,
                                            isStoreOnline = chat.isStoreOnline,
                                            storePhone = chat.storePhone
                                        )
                                        setListener(chatHead, chatHeads.size)
                                        chatHeads.add(chatHead)
                                        if (document.equals(result.last())) {
                                            this.listener.onChatHeadsReady(chatHeads)
                                        }
                                    }
                                }
                            }
                    }
                }
            }
    }

    private fun setListener(chatHead: ChatHead, position: Int) {
        FirebaseReferences.chatRef.document(chatHead.chatId).addSnapshotListener { value, error ->
            if (error != null)
                return@addSnapshotListener

            if (value != null) {
                val chat: Chat = value.toObject()!!
                if (chat.productIDs.last() != chatHead.productID) {
                    FirebaseReferences.productsRef
                        .document(chat.productIDs.last()).get()
                        .addOnSuccessListener { productDocument ->
                            if (productDocument != null) {
                                val product = productDocument.toObject<Product>()!!
                                chatHead.productID = chat.productIDs.last()
                                chatHead.storeId = product.storeID
                                chatHead.productName = product.name
                                chatHead.price = product.price
                                chatHead.productImageURL = product.images.first()
                            }
                        }
                }
                chatHead.isStoreOnline = chat.isStoreOnline
                chatHead.numOfMessage = chat.unreadCustomerMessages
                listener.onChatHeadChanged(position)
            }
        }
    }

}