package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.R
import com.shoplex.shoplex.model.pojo.ChatHead

class StoreHeadAdapter (private val storeHead: ArrayList<ChatHead>) :
        RecyclerView.Adapter<StoreHeadAdapter.ViewHolder>() {

    // var chatHead = listOf<ChatHead>()
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image : ImageView = view.findViewById(R.id.imgStoreHead)


    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.store_item_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = storeHead[position]
        Glide.with(viewHolder.itemView.context).load(item.storeImage).into(viewHolder.image)

        //viewHolder.image.setImageResource(item.productImageUrl.toInt())

    }

    override fun getItemCount() = storeHead.size


}