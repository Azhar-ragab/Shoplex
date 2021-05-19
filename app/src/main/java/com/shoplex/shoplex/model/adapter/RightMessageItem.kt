package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ChatItemRightBinding
import com.shoplex.shoplex.model.pojo.Message
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


class RightMessageItem(val message: Message) : Item<GroupieViewHolder>(){
    private lateinit var binding: ChatItemRightBinding
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        binding = ChatItemRightBinding.inflate(LayoutInflater.from(viewHolder.root.context))
        binding.tvSendMessage.text = message.message
        binding.tvRightDate.text = message.messageDate.toString()
    }
    override fun getLayout(): Int {
        return R.layout.chat_item_right
    }

}