package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shoplex.shoplex.databinding.SubCategoryItemBinding

class SubCategoryAdapter(val subCategories: Array<String>, val checkList: ArrayList<String>) :
    RecyclerView.Adapter<SubCategoryAdapter.StringViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        return StringViewHolder(
            SubCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: StringViewHolder, position: Int) =
        holder.bind(subCategories[position])

    override fun getItemCount() = subCategories.size

    inner class StringViewHolder(val binding: SubCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.cbSubCategoryName.text = item
            binding.cbSubCategoryName.setOnClickListener {
                if(binding.cbSubCategoryName.isChecked)
                    checkList.add(item)
                else
                    checkList.remove(item)
            }

            if(checkList.contains(item))
                binding.cbSubCategoryName.isChecked = true
        }
    }
}