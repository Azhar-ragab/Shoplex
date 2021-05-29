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
import com.shoplex.shoplex.databinding.ChatHeadItemRowBinding
import com.shoplex.shoplex.databinding.StoreItemRowBinding
import com.shoplex.shoplex.model.pojo.ChatHead
import com.shoplex.shoplex.view.activities.MessageActivity

//class StoreHeadAdapter (private val storeHead: ArrayList<ChatHead>) :
//        RecyclerView.Adapter<StoreHeadAdapter.ViewHolder>() {
//
//    // var chatHead = listOf<ChatHead>()
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val image : ImageView = view.findViewById(R.id.imgStoreHead)
//
//
//    }
//
//    // Create new views (invoked by the layout manager)
//    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
//        // Create a new view, which defines the UI of the list item
//        val view = LayoutInflater.from(viewGroup.context)
//                .inflate(R.layout.store_item_row, viewGroup, false)
//
//        return ViewHolder(view)
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//        val item = storeHead[position]
//        Glide.with(viewHolder.itemView.context).load(item.productImageURL).into(viewHolder.image)
//
//        //viewHolder.image.setImageResource(item.productImageUrl.toInt())
//
//    }
//
//    override fun getItemCount() = storeHead.size
//
//
//}
class StoreHeadAdapter(private val storeHead: ArrayList<ChatHead>) :
    RecyclerView.Adapter<StoreHeadAdapter.StoreHeadViewHolder>() {



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): StoreHeadViewHolder {
        return StoreHeadViewHolder(
            StoreItemRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: StoreHeadViewHolder, position: Int) {
        viewHolder.bind(storeHead[position])
    }

    override fun getItemCount() = storeHead.size

    inner class StoreHeadViewHolder(val binding: StoreItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(storeHead: ChatHead) {
                 Glide.with(itemView.context).load(storeHead.productImageURL).into(binding.imgStoreHead)

            }
        }
    }

