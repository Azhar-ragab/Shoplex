package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentDeliveryBinding
import com.shoplex.shoplex.databinding.FragmentPaymentBinding
import com.shoplex.shoplex.model.enumurations.DeliveryMethod
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.model.pojo.Checkout
import com.shoplex.shoplex.view.activities.CheckOutActivity

class PaymentFragment : Fragment() {

    lateinit var binding: FragmentPaymentBinding

    lateinit var checkout: Checkout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPaymentBinding.inflate(inflater,container,false)
        checkout = (activity as CheckOutActivity).checkout

        binding.rbFawry.setOnClickListener{
            checkout.paymentMethod = PaymentMethod.Fawry
            Toast.makeText(context, checkout.paymentMethod.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.rbCash.setOnClickListener {
            checkout.paymentMethod = PaymentMethod.Cash
            Toast.makeText(context, checkout.paymentMethod.toString(), Toast.LENGTH_SHORT).show()
        }


        // Inflate the layout for this fragment
        return binding.root
    }
}