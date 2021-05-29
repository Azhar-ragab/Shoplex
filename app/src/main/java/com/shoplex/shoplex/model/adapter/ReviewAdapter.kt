package com.shoplex.shoplex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.databinding.ReveiwItemBinding




class ReviewAdapter(val reviews: ArrayList<Review>) :
    RecyclerView.Adapter<ReviewAdapter.reviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): reviewViewHolder {
        return reviewViewHolder(
            ReveiwItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: reviewViewHolder, position: Int) =
        holder.bind(reviews[position])

    override fun getItemCount() = reviews.size

    inner class reviewViewHolder(val binding: ReveiwItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            // Your custom view code here
            Glide.with(itemView.context).load(review.image).into(binding.imgHead)
        binding.tvCustomerName.text = review.customerName
        binding.ratingBar.rating = review.rate
            binding.tvDate.text = review.date.toString()
            binding.tvComment.text = review.comment.toString()
            }
        }
    }
