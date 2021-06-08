package com.shoplex.shoplex.model.adapter

import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ChatItemRightBinding
import com.shoplex.shoplex.model.pojo.Message
import com.xwray.groupie.databinding.BindableItem

class RightMessageItem(val message: Message) : BindableItem<ChatItemRightBinding>(){

    override fun bind(binding: ChatItemRightBinding, position: Int) {
        binding.message = message
    }

    override fun getLayout(): Int {
        return R.layout.chat_item_right
    }
}