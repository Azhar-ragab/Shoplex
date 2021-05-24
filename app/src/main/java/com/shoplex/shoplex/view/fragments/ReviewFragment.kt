package com.shoplex.shoplex

import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.shoplex.shoplex.databinding.DialogAddReviewBinding
import com.shoplex.shoplex.databinding.FragmentReviewBinding
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import java.util.*
import kotlin.collections.ArrayList


class ReviewFragment(val productId: String) : Fragment() {

    lateinit var binding: FragmentReviewBinding
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReviewBinding.inflate(inflater, container, false)
        val review = ArrayList<Review>()
        val attrs: AttributeSet? = null

        review.add(
            Review(
                "azhar",
                "https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg",
                "Which review is likely to influence someone with an intense pizza craving? A five-star rating and “good pizza” is not bad, but it doesn’t have the same impact. A review doesn’t have to be the length of War and Peace, but an honest, detailed, and specific recollection goes a long way to building credibility.",

                Date(15), 5F
            )
        )
        review.add(
            Review(
                "azhar",
                "https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg",
                "Which review is likely to influence someone with an intense pizza craving? A five-star rating and “good pizza” is not bad, but it doesn’t have the same impact. A review doesn’t have to be the length of War and Peace, but an honest, detailed, and specific recollection goes a long way to building credibility.",

                Date(15), 5F
            )
        )
        review.add(
            Review(
                "azhar",
                "https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg",
                "Which review is likely to influence someone with an intense pizza craving? A five-star rating and “good pizza” is not bad, but it doesn’t have the same impact. A review doesn’t have to be the length of War and Peace, but an honest, detailed, and specific recollection goes a long way to building credibility.",

                Date(15), 5F
            )
        )
        review.add(
            Review(
                "azhar",
                "https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg",
                "Which review is likely to influence someone with an intense pizza craving? A five-star rating and “good pizza” is not bad, but it doesn’t have the same impact. A review doesn’t have to be the length of War and Peace, but an honest, detailed, and specific recollection goes a long way to building credibility.",

                Date(15), 5F
            )
        )

        reviewAdapter = ReviewAdapter(review)
        binding.rvReview.adapter = reviewAdapter

        binding.btnSheetAddReview.setOnClickListener {
            showAddReviewRialog()
        }
        return binding.root
    }

    private fun showAddReviewRialog() {
        val binding = DialogAddReviewBinding.inflate(layoutInflater)
        val reviewBtnSheetDialog = BottomSheetDialog(binding.root.context)

        binding.btnSendReview.setOnClickListener {
            val numStats = binding.rbAddReview.numStars
            val rate: Float = binding.rbAddReview.rating
            val reviewMsg = binding.edReview.text.toString()
            val review = Review(UserInfo.name,
                UserInfo.image,productId ,reviewMsg, Timestamp.now().toDate(), rate)
            FirebaseReferences.productsRef.document(productId).collection("Reviews").add(review)
            reviewBtnSheetDialog.dismiss()
        }
        reviewBtnSheetDialog.setContentView(binding.root)
        reviewBtnSheetDialog.show()

    }

}