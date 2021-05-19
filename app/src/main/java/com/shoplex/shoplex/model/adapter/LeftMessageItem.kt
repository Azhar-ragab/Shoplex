package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ChatItemLeftBinding
import com.xwray.groupie.GroupieViewHolder
import com.shoplex.shoplex.model.pojo.Message
import com.xwray.groupie.Item

class LeftMessageItem(val message: Message) : Item<GroupieViewHolder>(){
    private lateinit var binding: ChatItemLeftBinding
     override fun bind(viewHolder: GroupieViewHolder, position: Int) {
         binding = ChatItemLeftBinding.inflate(LayoutInflater.from(viewHolder.root.context))
         binding.tvReplyMessage.text = message.message
         binding.tvLeftDate.text = message.messageDate.toString()
    }

    override fun getLayout(): Int {
        return R.layout.chat_item_left
    }

}