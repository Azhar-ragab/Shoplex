package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentProductBinding
import com.shoplex.shoplex.databinding.FragmentReviewBinding
import com.shoplex.shoplex.databinding.FragmentSummaryBinding

import com.shoplex.shoplex.model.adapter.HomeProductsAdapter
import com.shoplex.shoplex.model.adapter.SummaryAdapter
import com.shoplex.shoplex.model.pojo.Summary_Checkout
import java.util.ArrayList


class SummaryFragment : Fragment() {
    private lateinit var binding: FragmentSummaryBinding
    private lateinit var summaryAdapter: SummaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val summary = ArrayList<Summary_Checkout>()
        // Inflate the layout for this fragment
        binding= FragmentSummaryBinding.inflate(inflater,container,false)
        summary.add(
            Summary_Checkout(
                "T-Shirt",
                "Quantity : 2",
                43.5F,
                "https://www.5wpr.com/new/wp-content/uploads/2016/04/fashion-public-relations.jpg"
            )
        )
        summary.add(
            Summary_Checkout(
"Pants",
                "Quantity : 4",
                120F,
                "https://images.unsplash.com/photo-1483985988355-763728e1935b?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8ZmFzaGlvbnxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=1000&q=80"
            )
        )
        summaryAdapter = SummaryAdapter(summary)


        binding.rvSummary.adapter = summaryAdapter


        return binding.root
    }


}