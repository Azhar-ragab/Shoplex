package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.R
import com.shoplex.shoplex.model.pojo.Products_Home
import com.shoplex.shoplex.model.pojo.Summary_Checkout

class SummaryAdapter(private val summarycheck: ArrayList<Summary_Checkout>) :
    RecyclerView.Adapter<SummaryAdapter.ViewHolder>() {  // Create a new view, which defines the UI of the list item
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.rv_summary_cardview, viewGroup, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.img_product_summary)
        val quantity: TextView = view.findViewById(R.id.tv_Quantity)
        val productName: TextView = view.findViewById(R.id.tv_product_name)
        val price: TextView = view.findViewById(R.id.tv_price_simmary)


    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = summarycheck[position]
        holder.productName.text = item.productName
        holder.quantity.text = item.Quantity
        holder.price.text = item.Price.toString()
        Glide.with(holder.itemView.context).load(item.productImageURL).into(holder.img)
    }

    override fun getItemCount() = summarycheck.size


}