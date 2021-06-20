package com.shoplex.shoplex.view.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.shoplex.shoplex.databinding.FragmentDeliveryBinding
import com.shoplex.shoplex.model.enumurations.DeliveryMethod
import com.shoplex.shoplex.model.enumurations.LocationAction
import com.shoplex.shoplex.model.pojo.Location
import com.shoplex.shoplex.view.activities.CheckOutActivity
import com.shoplex.shoplex.view.activities.MapsActivity
import com.shoplex.shoplex.viewmodel.CheckoutVM

class DeliveryFragment : Fragment() {
    private lateinit var binding: FragmentDeliveryBinding
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

        binding.rgDelivery.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioDoorDelivery.id -> checkoutVM.deliveryMethod.value =
                    DeliveryMethod.Door
                binding.radioPostStation.id -> checkoutVM.deliveryMethod.value =
                    DeliveryMethod.Post_Station
            }
        }

        checkoutVM.subTotalPrice.observe(viewLifecycleOwner, {
            binding.tvSubtotalPrice.text = "$it EGP"
        })

        checkoutVM.totalDiscount.observe(viewLifecycleOwner, {
            binding.tvDiscountPrice.text = "$it EGP"
        })

        checkoutVM.shipping.observe(viewLifecycleOwner, {
            binding.tvShippingPrice.text = "$it EGP"
        })


        checkoutVM.totalPrice.observe(viewLifecycleOwner, {
            binding.tvTotalPrice.text = "$it EGP"
        })

        checkoutVM.deliveryAddress.observe(viewLifecycleOwner, {
            binding.tvAddress.text = it
        })

        binding.btnDelivery.setOnClickListener {
            val pager = (activity as CheckOutActivity).binding.viewPagerCheckout
            pager.currentItem = pager.currentItem + 1
        }

        binding.btnChangeAddress.setOnClickListener {
            startActivityLaunch.launch(Intent(context, MapsActivity::class.java).apply {
                this.putExtra(MapsActivity.LOCATION_ACTION, LocationAction.Change.name)
                this.putExtra(MapsActivity.LOCATION, checkoutVM.deliveryLocation.value)
            })
        }
        return binding.root
    }

    private fun onMapResult(data: Intent) {

        val location: LatLng? = data.getParcelableExtra(MapsActivity.LOCATION)
        val address: String? = data.getStringExtra(MapsActivity.ADDRESS)
        if (location != null && (checkoutVM.deliveryLocation.value!!.latitude != location.latitude || checkoutVM.deliveryLocation.value!!.longitude != location.longitude)) {
            binding.tvAddress.text = address
            checkoutVM.deliveryAddress.value = address
            checkoutVM.deliveryLocation.value = Location(location.latitude, location.longitude)
            checkoutVM.reAddShipping()
        }
    }
}