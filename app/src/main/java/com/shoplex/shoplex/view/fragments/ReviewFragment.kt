package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.DialogAddReviewBinding
import com.shoplex.shoplex.databinding.FragmentReviewBinding
import com.shoplex.shoplex.model.adapter.ReviewAdapter
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Review
import com.shoplex.shoplex.viewmodel.ProductsVM

class ReviewFragment(val productId: String) : Fragment() {

    lateinit var binding: FragmentReviewBinding
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var productsVM: ProductsVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReviewBinding.inflate(inflater, container, false)
        this.productsVM = ProductsVM()
        productsVM.getReviewByProductId(productId)

        productsVM.reviews.observe(viewLifecycleOwner, { reviews ->
            reviewAdapter = ReviewAdapter(reviews)
            binding.rvReview.adapter = reviewAdapter
        })

        productsVM.reviewStatistics.observe(viewLifecycleOwner, {
            binding.reviewStat = it
            if(it.total != 0) {
                binding.fiveStars.progress = ((it.fiveStars.toFloat() / it.total) * 100).toInt()
                binding.fourStars.progress = ((it.fourStars.toFloat() / it.total) * 100).toInt()
                binding.threeStars.progress = ((it.threeStars.toFloat() / it.total) * 100).toInt()
                binding.twoStars.progress = ((it.twoStars.toFloat() / it.total) * 100).toInt()
                binding.oneStar.progress = ((it.oneStar.toFloat() / it.total) * 100).toInt()
            }
        })

        /*
        productsVM.reviewStatistics.observe(viewLifecycleOwner, {

        })
        */

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
            FirebaseReferences.productsRef.document(productId).collection(getString(R.string.Reviews)).add(review)
            reviewBtnSheetDialog.dismiss()
        }
        reviewBtnSheetDialog.setContentView(binding.root)
        reviewBtnSheetDialog.show()
    }
}