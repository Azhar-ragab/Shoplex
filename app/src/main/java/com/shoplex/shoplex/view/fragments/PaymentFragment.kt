package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentPaymentBinding
import com.shoplex.shoplex.model.enumurations.DiscountType
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.model.pojo.Checkout
import com.shoplex.shoplex.view.activities.CheckOutActivity
import com.shoplex.shoplex.viewmodel.CheckoutVM

class PaymentFragment : Fragment() {

    lateinit var binding: FragmentPaymentBinding
    lateinit var checkout: Checkout
    var ischecked = false

    private lateinit var checkoutVM: CheckoutVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        checkout = (activity as CheckOutActivity).checkout
        checkoutVM = (activity as CheckOutActivity).checkoutVM

        binding.rgPayment.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbCash.id -> {
                    checkoutVM.paymentMethod.value = PaymentMethod.Cash
                    ischecked = true
                    //Toast.makeText(context, binding.rbCash.text.toString(), Toast.LENGTH_SHORT).show()
                }
                binding.rbFawry.id -> {
                    checkoutVM.paymentMethod.value = PaymentMethod.Fawry
                    ischecked = true
                    //Toast.makeText(context, binding.rbFawry.text.toString(), Toast.LENGTH_SHORT).show()
                }
                binding.rbVodafone.id -> {
                    checkoutVM.paymentMethod.value = PaymentMethod.Vodafone_Cash
                    ischecked = true
                    //Toast.makeText(context, binding.rbVodafone.text.toString(), Toast.LENGTH_SHORT).show()
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
        binding.tvCopoun.text =getString(R.string.DiscountBy) + checkout.totalDiscount
        binding.btnPayment.setOnClickListener {
            if (ischecked) {
                var pager = (activity as CheckOutActivity).binding.viewPagerCheckout
                pager.currentItem = pager.currentItem + 1
            } else {
                Toast.makeText(context, getString(R.string.PaymentMethod), Toast.LENGTH_SHORT).show()
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}