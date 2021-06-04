package com.shoplex.shoplex.model.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ChatHeadItemRowBinding
import com.shoplex.shoplex.model.enumurations.keys.ChatMessageKeys
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.pojo.Chat
import com.shoplex.shoplex.model.pojo.ChatHead
import com.shoplex.shoplex.view.activities.MessageActivity


class ChatHeadAdapter(private val chatHeads: ArrayList<ChatHead>) :
    RecyclerView.Adapter<ChatHeadAdapter.ChatHeadViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChatHeadViewHolder {
        return ChatHeadViewHolder(
            ChatHeadItemRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ChatHeadViewHolder, position: Int) {
        //viewHolder.bind(chatHead[position])
        setListener(viewHolder, chatHeads[position])
    }

    private fun setListener(viewHolder: ChatHeadViewHolder, chatHead: ChatHead){
        FirebaseReferences.chatRef.document(chatHead.chatId).addSnapshotListener { value, error ->
            if(error != null)
                return@addSnapshotListener

            if(value != null) {
                val chat: Chat = value.toObject()!!
                /*
                if(chat.productIDs.size != chatHead.productsIDs.size) {
                    FirebaseReferences.productsRef
                        .document(chat.productIDs.last()).get()
                        .addOnSuccessListener { productDocument ->
                            if (productDocument != null) {
                                val product = productDocument.toObject<Product>()!!
                                chatHead.productsIDs = chat.productIDs
                                chatHead.storeId = product.storeID
                                chatHead.productName = product.name
                                chatHead.price = product.price
                                chatHead.productImageURL = product.images[0]
                                chatHead.numOfMessage = chat.unreadStoreMessages
                            }
                        }
                }
                */
                chatHead.numOfMessage = chat.unreadCustomerMessages
                viewHolder.bind(chatHead)
            }
        }
    }

    override fun getItemCount() = chatHeads.size

    inner class ChatHeadViewHolder(val binding: ChatHeadItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chatHead: ChatHead) {
            Glide.with(itemView.context).load(chatHead.productImageURL).into(binding.imgChatHead)
            binding.tvUserNameChatHead.text = chatHead.userName
            binding.tvProductNameChatHead.text = chatHead.productName
            binding.tvNumOfMessage.text = chatHead.numOfMessage.toString()
            binding.tvPriceChatHeader.text = chatHead.price.toString()
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, MessageActivity::class.java)
                intent.putExtra(ChatMessageKeys.CHAT_TITLE_KEY.name, chatHead.userName)
                intent.putExtra(ChatMessageKeys.CHAT_IMG_KEY.name,chatHead.productImageURL)
                intent.putExtra(ChatMessageKeys.CHAT_ID.name,chatHead.chatId)
                // intent.putExtra(ChatMessageKeys.USER_ID_KEY.name,chatHead.userID)

                itemView.context.startActivity(intent)
            }
        }
    }

}