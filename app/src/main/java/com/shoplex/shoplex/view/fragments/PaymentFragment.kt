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
import com.shoplex.shoplex.model.enumurations.DiscountType
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.model.pojo.Checkout
import com.shoplex.shoplex.model.pojo.SpecialDiscount
import com.shoplex.shoplex.view.activities.CheckOutActivity

class PaymentFragment : Fragment() {

    lateinit var binding: FragmentPaymentBinding
    lateinit var checkout: Checkout
    var ischecked = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        checkout = (activity as CheckOutActivity).checkout

        binding.rgPayment.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_cash -> {
                    checkout.paymentMethod = PaymentMethod.Cash
                    ischecked = true
                    Toast.makeText(context, binding.rbCash.text.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.rb_fawry -> {
                    checkout.paymentMethod = PaymentMethod.Fawry
                    ischecked = true
                    Toast.makeText(context, binding.rbFawry.text.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.rb_vodafon -> {
                    checkout.paymentMethod = PaymentMethod.Vodafone_Cash
                    ischecked = true
                    Toast.makeText(context, binding.rbVodafon.text.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> ischecked = false
            }
        }
        var copoun = 0.0F
        for (discount in checkout.getAllProducts()) {
            var discountValue = discount.specialDiscount?.discount
            when (discount.specialDiscount?.discountType) {
                DiscountType.Fixed -> copoun += discountValue!!
                DiscountType.Percentage -> copoun += discountValue!!
            }

        }

        binding.tvSubtotalPrice.text = "${checkout.subTotalPrice} EGP"
        binding.tvDiscountPrice.text = "${checkout.totalDiscount} EGP"
        binding.tvShippingPrice.text = "${checkout.shipping} EGP"
        binding.tvTotalPrice.text = "${checkout.totalPrice} EGP"
        binding.tvCopoun.text = "Discount By ${copoun}"
        binding.btnPayment.setOnClickListener {
            if (ischecked == true) {
                var pager = (activity as CheckOutActivity).binding.viewPagerCheckout
                pager.currentItem = pager.currentItem + 1
            } else {
                Toast.makeText(context, "Choose Payment Method", Toast.LENGTH_SHORT).show()
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}