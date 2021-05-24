package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shoplex.shoplex.databinding.ShopItemBinding
import com.shoplex.shoplex.model.pojo.StoreLocationInfo

class StoresLocationsAdapter(val stores: ArrayList<StoreLocationInfo>) :
    RecyclerView.Adapter<StoresLocationsAdapter.StoreLocationInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreLocationInfoViewHolder {
        return StoreLocationInfoViewHolder(
            ShopItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: StoreLocationInfoViewHolder, position: Int) =
        holder.bind(stores[position])

    override fun getItemCount() = stores.size

    inner class StoreLocationInfoViewHolder(val binding: ShopItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(locationInfo: StoreLocationInfo) {
            binding.cbShopName.text = locationInfo.storeName
            binding.tvDistance.text = locationInfo.distance.toString()
            binding.tvWalking.text = locationInfo.walkingTime.toString()
            binding.tvCar.text = locationInfo.carTime.toString()
        }
    }
}