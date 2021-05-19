package com.shoplex.shoplex.model.adapter

import com.shoplex.shoplex.R
import com.shoplex.shoplex.model.pojo.Message
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_item_right.view.*


class RightMessageItem(val message: Message) : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tvSendMessage.text = message.message
        viewHolder.itemView.tvRightDate.text = message.messageDate.toString()
    }
    override fun getLayout(): Int {
        return R.layout.chat_item_right
    }

}