package com.shoplex.shoplex.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentSummaryBinding

import com.shoplex.shoplex.model.adapter.HomeProductsAdapter
import com.shoplex.shoplex.model.adapter.SummaryAdapter
import com.shoplex.shoplex.model.enumurations.OrderStatus
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Checkout
import com.shoplex.shoplex.model.pojo.Order
import com.shoplex.shoplex.model.pojo.Summary_Checkout
import com.shoplex.shoplex.model.pojo.User
import com.shoplex.shoplex.view.activities.CheckOutActivity
import com.shoplex.shoplex.view.activities.HomeActivity
import com.shoplex.shoplex.viewmodel.CheckoutVM
import java.util.ArrayList


class SummaryFragment : Fragment() {
    private lateinit var binding: FragmentSummaryBinding
    private lateinit var summaryAdapter: SummaryAdapter
    lateinit var checkout: Checkout

    private lateinit var checkoutVM: CheckoutVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSummaryBinding.inflate(inflater, container, false)
        checkout = (activity as CheckOutActivity).checkout

        checkoutVM = (activity as CheckOutActivity).checkoutVM

        summaryAdapter = SummaryAdapter(checkout.getAllProducts())
        binding.rvSummary.adapter = summaryAdapter

        binding.tvItemNum.text = "${checkout.getAllProducts().size} Items"
        binding.tvSubtotalPrice.text = "${checkout.subTotalPrice} EGP"
        binding.tvDiscountPrice.text = "${checkout.totalDiscount} EGP"
        binding.tvShippingPrice.text = "${checkout.shipping} EGP"
        binding.tvTotalPrice.text = "${checkout.totalPrice} EGP"

        binding.btnSummary.setOnClickListener {
            for (product in checkout.getAllProducts()){
                val order = Order(product, checkout, OrderStatus.Current)
                FirebaseReferences.ordersRef.document(order.orderID).set(order).addOnSuccessListener {
                    Toast.makeText(context,getString(R.string.Success), Toast.LENGTH_SHORT).show()
                }
            }
            startActivity(Intent(context,HomeActivity::class.java))
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        checkoutVM.deliveryMethod.observe(viewLifecycleOwner, Observer{
            binding.tvDeliveryStatue.text = it.toString()
            checkout.deliveryMethod = it
        })

        checkoutVM.paymentMethod.observe(viewLifecycleOwner, Observer{
            binding.tvPaymentStatue.text = it.toString()
            checkout.paymentMethod = it
        })
    }
}