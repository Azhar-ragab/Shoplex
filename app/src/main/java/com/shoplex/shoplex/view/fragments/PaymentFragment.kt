package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentPaymentBinding
import com.shoplex.shoplex.model.enumurations.DiscountType
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.view.activities.CheckOutActivity
import com.shoplex.shoplex.viewmodel.CheckoutVM

class PaymentFragment : Fragment() {

    lateinit var binding: FragmentPaymentBinding
    // lateinit var checkout: Checkout
    //var ischecked = false

    private lateinit var checkoutVM: CheckoutVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        checkoutVM = (activity as CheckOutActivity).checkoutVM
        // checkout = checkoutVM.checkout.value!!

        binding.rgPayment.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbCash.id -> {
                    checkoutVM.paymentMethod.value = PaymentMethod.Cash
                    //ischecked = true
                    //Toast.makeText(context, binding.rbCash.text.toString(), Toast.LENGTH_SHORT).show()
                }
                binding.rbVisaMaster.id -> {
                    checkoutVM.paymentMethod.value = PaymentMethod.Visa_Master
                    //ischecked = true
                    //Toast.makeText(context, binding.rbFawry.text.toString(), Toast.LENGTH_SHORT).show()
                }
                //else -> ischecked = false
            }
        }

        var copoun = 0.0F
        for (discount in checkoutVM.getAllProducts()) {
            var discountValue = discount.specialDiscount?.discount
            when (discount.specialDiscount?.discountType) {
                DiscountType.Fixed -> copoun += discountValue!!
                DiscountType.Percentage -> copoun += discountValue!!
            }
        }

        checkoutVM.subTotalPrice.observe(viewLifecycleOwner, {
            binding.tvSubtotalPrice.text = "$it ${requireContext().getString(R.string.EGP)}"
        })

        checkoutVM.totalDiscount.observe(viewLifecycleOwner, {
            binding.tvDiscountPrice.text = "$it ${requireContext().getString(R.string.EGP)}"
        })

        checkoutVM.shipping.observe(viewLifecycleOwner, {
            binding.tvShippingPrice.text = "$it ${requireContext().getString(R.string.EGP)}"
        })

        checkoutVM.totalPrice.observe(viewLifecycleOwner, {
            binding.tvTotalPrice.text = "$it ${requireContext().getString(R.string.EGP)}"
        })

        //binding.total = checkout.totalPrice.value.toString()

//        binding.tvSubtotalPrice.text = "${checkout.subTotalPrice.value} EGP"
//        binding.tvDiscountPrice.text = "${checkout.totalDiscount.value} EGP"
//        binding.tvShippingPrice.text = "${checkout.shipping.value} EGP"
//        binding.tvTotalPrice.text = "${checkout.totalPrice.value} EGP"
        binding.tvCopoun.text = "${getString(R.string.DiscountBy)} ${checkoutVM.coupons.value}"
        binding.btnPayment.setOnClickListener {

            var pager = (activity as CheckOutActivity).binding.viewPagerCheckout
            pager.currentItem = pager.currentItem + 1
//            if (ischecked) {
//
//            } else {
//                Toast.makeText(context, getString(R.string.PaymentMethod), Toast.LENGTH_SHORT).show()
//            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}