package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentDeliveryBinding
import com.shoplex.shoplex.model.enumurations.DeliveryMethod
import com.shoplex.shoplex.model.pojo.Checkout
import com.shoplex.shoplex.view.activities.CheckOutActivity


class DeliveryFragment : Fragment() {

    lateinit var binding: FragmentDeliveryBinding
    lateinit var checkout: Checkout
    var ischecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeliveryBinding.inflate(inflater, container, false)
        checkout = (activity as CheckOutActivity).checkout

        binding.rgDelivery.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioDoorDelivery -> {
                    checkout.deliveryMethod = DeliveryMethod.Door
                    ischecked = true
                    Toast.makeText(
                        context,
                        binding.radioDoorDelivery.text.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                R.id.radioPostStation -> {
                    checkout.deliveryMethod = DeliveryMethod.Post_Station
                    ischecked = true
                    Toast.makeText(
                        context,
                        binding.radioPostStation.text.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> ischecked = false
            }
        }

        binding.tvSubtotalPrice.text = "${checkout.subTotalPrice} EGP"
        binding.tvDiscountPrice.text = "${checkout.totalDiscount} EGP"
        binding.tvShippingPrice.text = "${checkout.shipping} EGP"
        binding.tvTotalPrice.text = "${checkout.totalPrice} EGP"
        binding.btnDelivery.setOnClickListener {
            if (ischecked == true) {
                var pager = (activity as CheckOutActivity).binding.viewPagerCheckout
                pager.currentItem = pager.currentItem + 1
            } else {
                Toast.makeText(context, "Choose Delivery Method", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}