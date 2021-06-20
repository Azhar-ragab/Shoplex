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

    private lateinit var binding: FragmentPaymentBinding
    private lateinit var checkoutVM: CheckoutVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        checkoutVM = (activity as CheckOutActivity).checkoutVM

        binding.rgPayment.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbCash.id -> checkoutVM.paymentMethod.value = PaymentMethod.Cash
                binding.rbVisaMaster.id -> checkoutVM.paymentMethod.value =
                    PaymentMethod.Visa_Master
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

        binding.tvCopoun.text = "${getString(R.string.DiscountBy)} ${checkoutVM.coupons.value}"
        binding.btnPayment.setOnClickListener {

            val pager = (activity as CheckOutActivity).binding.viewPagerCheckout
            pager.currentItem = pager.currentItem + 1
        }

        return binding.root
    }
}