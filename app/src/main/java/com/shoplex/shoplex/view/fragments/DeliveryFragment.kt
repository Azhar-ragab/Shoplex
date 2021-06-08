package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentDeliveryBinding
import com.shoplex.shoplex.model.enumurations.DeliveryMethod
import com.shoplex.shoplex.model.pojo.Checkout
import com.shoplex.shoplex.model.pojo.Location
import com.shoplex.shoplex.view.activities.CheckOutActivity
import com.shoplex.shoplex.viewmodel.CheckoutVM

class DeliveryFragment : Fragment() {
    private lateinit var binding: FragmentDeliveryBinding
    private lateinit var checkout: Checkout
    private var isChecked = false

    private lateinit var checkoutVM: CheckoutVM


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeliveryBinding.inflate(inflater, container, false)
        checkout = (activity as CheckOutActivity).checkout
        checkoutVM = (activity as CheckOutActivity).checkoutVM

        checkout.deliveryMethod = DeliveryMethod.Door

        binding.rgDelivery.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioDoorDelivery.id -> {
                    checkoutVM.deliveryMethod.value = DeliveryMethod.Door
                    isChecked = true
                    // Toast.makeText(context, binding.radioDoorDelivery.text.toString(), Toast.LENGTH_SHORT).show()
                }
                binding.radioPostStation.id -> {
                    checkoutVM.deliveryMethod.value = DeliveryMethod.Post_Station
                    isChecked = true
                    // Toast.makeText(context, binding.radioPostStation.text.toString(), Toast.LENGTH_SHORT).show()
                }
                else -> isChecked = false
            }
        }

        binding.tvSubtotalPrice.text = "${checkout.subTotalPrice} EGP"
        binding.tvDiscountPrice.text = "${checkout.totalDiscount} EGP"
        binding.tvShippingPrice.text = "${checkout.shipping} EGP"
        binding.tvTotalPrice.text = "${checkout.totalPrice} EGP"
        binding.btnDelivery.setOnClickListener {
            if (isChecked) {
                var pager = (activity as CheckOutActivity).binding.viewPagerCheckout
                pager.currentItem = pager.currentItem + 1
            } else {
                Toast.makeText(context, getString(R.string.deliveryMethod), Toast.LENGTH_SHORT).show()
            }
        }

        checkout.deliveryAddress = binding.tvAddress.text.toString()
        checkout.deliveryLoc = Location(31.1688133,29.931152)

        binding.tvAddress.addTextChangedListener {
            checkout.deliveryAddress = binding.tvAddress.text.toString()
            checkout.deliveryLoc = Location(31.1688133,29.931152)
        }
        // checkoutVM.deliveryAddress.value = binding.tvAddress.text.toString()
        // checkoutVM.deliveryLocation.value = Location(31.1688133,29.931152)

        return binding.root
    }
}