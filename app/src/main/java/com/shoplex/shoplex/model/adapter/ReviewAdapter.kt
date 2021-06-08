package com.shoplex.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.databinding.ReveiwItemBinding
import com.shoplex.shoplex.model.pojo.Review

class ReviewAdapter(val reviews: ArrayList<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            ReveiwItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) =
        holder.bind(reviews[position])

    override fun getItemCount() = reviews.size

    inner class ReviewViewHolder(val binding: ReveiwItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            // Your custom view code here
            binding.review = review
            Glide.with(itemView.context).load(review.image).into(binding.imgHead)
//        binding.tvCustomerName.text = review.customerName
             binding.ratingBar.rating = review.rate
//            binding.tvDate.text = review.date.toString()
//            binding.tvComment.text = review.comment.toString()
        }
    }
}
