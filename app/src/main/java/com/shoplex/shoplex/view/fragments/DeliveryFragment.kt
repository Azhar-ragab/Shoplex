package com.shoplex.shoplex.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.shoplex.shoplex.databinding.FragmentDeliveryBinding
import com.shoplex.shoplex.model.enumurations.DeliveryMethod
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.model.pojo.Checkout
import com.shoplex.shoplex.view.activities.CheckOutActivity


class DeliveryFragment : Fragment() {

    lateinit var binding: FragmentDeliveryBinding
    lateinit var checkout: Checkout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeliveryBinding.inflate(inflater, container, false)
        checkout = (activity as CheckOutActivity).checkout

        checkout.deliveryMethod = DeliveryMethod.Post_Station

        return binding.root
    }
}