package com.shoplex.shoplex.model.adapter

import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ChatItemLeftBinding
import com.shoplex.shoplex.model.pojo.Message
import com.xwray.groupie.databinding.BindableItem

class LeftMessageItem(val message: Message) : BindableItem<ChatItemLeftBinding>(){

    override fun bind(binding: ChatItemLeftBinding, position: Int) {
        binding.message = message
    }

    override fun getLayout(): Int {
        return R.layout.chat_item_left
    }

}