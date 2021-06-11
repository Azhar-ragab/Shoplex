package com.shoplex.shoplex.view.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentDeliveryBinding
import com.shoplex.shoplex.model.enumurations.Category
import com.shoplex.shoplex.model.enumurations.DeliveryMethod
import com.shoplex.shoplex.model.enumurations.LocationAction
import com.shoplex.shoplex.model.pojo.Checkout
import com.shoplex.shoplex.model.pojo.Filter
import com.shoplex.shoplex.model.pojo.Location
import com.shoplex.shoplex.model.pojo.Sort
import com.shoplex.shoplex.view.activities.CheckOutActivity
import com.shoplex.shoplex.view.activities.FilterActivity
import com.shoplex.shoplex.view.activities.MapsActivity
import com.shoplex.shoplex.viewmodel.CheckoutVM

class DeliveryFragment : Fragment() {
    private lateinit var binding: FragmentDeliveryBinding
    private lateinit var checkout: Checkout
    //private var isChecked = false
    private lateinit var checkoutVM: CheckoutVM
    private lateinit var startActivityLaunch: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivityLaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = it.data
                    if (data != null)
                        onMapResult(data)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeliveryBinding.inflate(inflater, container, false)

        checkoutVM = (activity as CheckOutActivity).checkoutVM
        checkout = checkoutVM.checkout.value!!

        //checkout.deliveryMethod = DeliveryMethod.Door

        binding.rgDelivery.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioDoorDelivery.id -> {
                    checkoutVM.deliveryMethod.value = DeliveryMethod.Door
                    //isChecked = true
                    // Toast.makeText(context, binding.radioDoorDelivery.text.toString(), Toast.LENGTH_SHORT).show()
                }
                binding.radioPostStation.id -> {
                    checkoutVM.deliveryMethod.value = DeliveryMethod.Post_Station
                    //isChecked = true
                    // Toast.makeText(context, binding.radioPostStation.text.toString(), Toast.LENGTH_SHORT).show()
                }
                //else -> isChecked = false
            }
        }

        checkoutVM.subTotal.observe(viewLifecycleOwner, {
            binding.tvSubtotalPrice.text = "$it EGP"
        })

        checkoutVM.discount.observe(viewLifecycleOwner, {
            binding.tvDiscountPrice.text = "$it EGP"
        })

        checkoutVM.shipping.observe(viewLifecycleOwner, {
            binding.tvShippingPrice.text = "$it EGP"
        })


        checkoutVM.total.observe(viewLifecycleOwner, {
            binding.tvTotalPrice.text = "$it EGP"
        })


        /*
        binding.tvDiscountPrice.text = "${checkout.totalDiscount} EGP"
        binding.tvShippingPrice.text = "${checkout.shipping} EGP"
        binding.tvTotalPrice.text = "${checkout.totalPrice} EGP"
        */
        binding.btnDelivery.setOnClickListener {
            var pager = (activity as CheckOutActivity).binding.viewPagerCheckout
            pager.currentItem = pager.currentItem + 1
            /*
            if (isChecked) {

            } else {
                Toast.makeText(context, getString(R.string.deliveryMethod), Toast.LENGTH_SHORT).show()
            }
            */
        }

        binding.btnChangeAddress.setOnClickListener {
            startActivityLaunch.launch(Intent(context, MapsActivity::class.java).apply {
                this.putExtra(MapsActivity.LOCATION_ACTION, LocationAction.Change.name)
            })
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

    private fun onMapResult(data: Intent) {

        val location: LatLng? = data.getParcelableExtra(MapsActivity.LOCATION)
        val address: String? = data.getStringExtra(MapsActivity.ADDRESS)
        if (location != null) {
            binding.tvAddress.text = address
            checkoutVM.deliveryAddress.value = address
            checkoutVM.deliveryLocation.value = Location(location.latitude, location.longitude)
        }
    }
}