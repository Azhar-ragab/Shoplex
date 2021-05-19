package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.R
import com.shoplex.shoplex.model.pojo.Products_Home

class HomeProductsAdapter(val productsHome: ArrayList<Product>) :
    RecyclerView.Adapter<HomeProductsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.rv_home_product_cardview, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productsHome[position]
        holder.storeName.text = item.storeName
        holder.newPrice.text = item.newPrice.toString()
        holder.oldPrice.text = item.price.toString()
        holder.productName.text = item.name
        holder.rating.text = item.rate.toString()
        holder.sold.text = "12"
        holder.space.text = "Space"
        holder.storeName.text = item.storeName
        if(item.images.count() > 0)
        Glide.with(holder.itemView.context).load(item.images[0]).into(holder.img)
    }

    override fun getItemCount() = productsHome.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.img_product)
        val oldPrice: TextView = view.findViewById(R.id.tv_old_price)
        val newPrice: TextView = view.findViewById(R.id.tv_new_price)
        val productName: TextView = view.findViewById(R.id.tv_product_name)
        val rating: TextView = view.findViewById(R.id.tv_review)
        val sold: TextView = view.findViewById(R.id.tv_sold)
        val storeName: TextView = view.findViewById(R.id.tv_storename)
        val space: TextView = view.findViewById(R.id.tv_space)
    }
}