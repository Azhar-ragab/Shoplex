package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.R
import com.shoplex.shoplex.model.pojo.Ads_Home

class AdvertisementAdapter(val advertisement: ArrayList<Ads_Home>) :
    RecyclerView.Adapter<AdvertisementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.rv_home_adcardview, viewGroup, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = advertisement[position]
        holder.name.text = item.productName
        holder.offer.text = item.productOffer
        Glide.with(holder.itemView.context).load(item.productImageURL).into(holder.background)
    }

    override fun getItemCount() = advertisement.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val background: ImageView = view.findViewById(R.id.img_advertisement)
        val name: TextView = view.findViewById(R.id.txt_advertisement)
        val offer: TextView = view.findViewById(R.id.tv_offer)
    }

}


