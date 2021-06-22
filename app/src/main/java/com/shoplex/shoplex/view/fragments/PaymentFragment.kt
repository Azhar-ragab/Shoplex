package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentPaymentBinding
import com.shoplex.shoplex.model.adapter.SpecialCouponAdapter
import com.shoplex.shoplex.model.enumurations.DiscountType
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.model.pojo.SpecialCoupon
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

        checkoutVM.isAllProductsReady.observe(viewLifecycleOwner, {
            if(it){
                //var coupon = 0.0F
                    val specialCoupons: ArrayList<SpecialCoupon> = arrayListOf()
                for (product in checkoutVM.getAllProducts()) {
                    if(product.specialDiscount != null){
                        specialCoupons.add(SpecialCoupon(product.name, product.specialDiscount!!.discount, product.specialDiscount!!.discountType))
                    }
                    /*
                    val discountValue = product.specialDiscount?.discount

                    when (discount.specialDiscount?.discountType) {
                        DiscountType.Fixed -> coupon += discountValue!!
                        DiscountType.Percentage -> coupon += discountValue!!
                    }
                    */
                }
                //binding.tvCopoun.text = coupon.toString()
                if(specialCoupons.isNotEmpty()){
                    binding.rvSpecialCoupons.adapter = SpecialCouponAdapter(specialCoupons)
                }else{
                    binding.tvSpecialCoupon.text = getString(R.string.DontHaveSpecialCoupons)
                }
            }
        })

        checkoutVM.subTotalPrice.observe(viewLifecycleOwner, {
            binding.tvSubtotalPrice.text = getString(R.string.EGP).format(it)
        })

        checkoutVM.totalDiscount.observe(viewLifecycleOwner, {
            binding.tvDiscountPrice.text = getString(R.string.EGP).format(it)
        })

        checkoutVM.shipping.observe(viewLifecycleOwner, {
            binding.tvShippingPrice.text = getString(R.string.EGP).format(it)
        })

        checkoutVM.totalPrice.observe(viewLifecycleOwner, {
            binding.tvTotalPrice.text = getString(R.string.EGP).format(it)
        })

        //binding.tvCopoun.text = getString(R.string.DiscountBy).format(checkoutVM.coupons.value)
        binding.btnPayment.setOnClickListener {

            val pager = (activity as CheckOutActivity).binding.viewPagerCheckout
            pager.currentItem = pager.currentItem + 1
        }

        return binding.root
    }
}