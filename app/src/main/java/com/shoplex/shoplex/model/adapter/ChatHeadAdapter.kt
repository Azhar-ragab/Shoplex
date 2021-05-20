package com.shoplex.shoplex.model.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.R
import com.shoplex.shoplex.model.pojo.ChatHead
import com.shoplex.shoplex.view.activities.MessageActivity


class ChatHeadAdapter (private val chatHead: ArrayList<ChatHead>) :
    RecyclerView.Adapter<ChatHeadAdapter.ViewHolder>() {
    companion object{
        val CHAT_TITLE_KEY = "CHAT_TITLE_KEY"
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.chat_head_item_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = chatHead[position]
        Glide.with(viewHolder.itemView.context).load(item.productImageUrl).into(viewHolder.image)
        viewHolder.userName.text = item.userName
        viewHolder.productName.text = item.productName
        viewHolder.numOfMessage.text = item.numOfMessage.toString()
        viewHolder.price.text = item.price.toString()
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, MessageActivity::class.java)
            intent.putExtra(CHAT_TITLE_KEY,item.userName)
            intent.putExtra("chatID","azhar")
            intent.putExtra("phoneNumber","01016512198")
            viewHolder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount() = chatHead.size
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image : ImageView = view.findViewById(R.id.imgChatHead)
        val userName : TextView = view.findViewById(R.id.tvUserNameChatHead)
        val productName : TextView = view.findViewById(R.id.tvProductNameChatHead)
        val numOfMessage :TextView = view.findViewById(R.id.tvNumOfMessage)
        val price : TextView = view.findViewById(R.id.tvPriceChatHeader)

    }

}