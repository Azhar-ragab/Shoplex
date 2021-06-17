package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FieldValue
import com.google.gson.Gson
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentSummaryBinding
import com.shoplex.shoplex.model.adapter.SummaryAdapter
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Order
import com.shoplex.shoplex.room.data.ShopLexDataBase
import com.shoplex.shoplex.room.repository.FavoriteCartRepo
import com.shoplex.shoplex.view.activities.CheckOutActivity
import com.shoplex.shoplex.viewmodel.CheckoutVM
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class SummaryFragment : Fragment() {
    private lateinit var binding: FragmentSummaryBinding
    private lateinit var summaryAdapter: SummaryAdapter
    // lateinit var checkout: Checkout
    private lateinit var checkoutVM: CheckoutVM

    private lateinit var paymentSheet: PaymentSheet
    private lateinit var paymentIntentClientSecret: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSummaryBinding.inflate(inflater, container, false)
        binding.btnSummary.isEnabled = false
        checkoutVM = (activity as CheckOutActivity).checkoutVM

        //checkout = checkoutVM.checkout.value!! //(activity as CheckOutActivity).checkout

        summaryAdapter = SummaryAdapter(checkoutVM.getAllProducts())
        binding.rvSummary.adapter = summaryAdapter

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
            if(checkoutVM.isAllProductsReady.value == true && checkoutVM.paymentMethod.value == PaymentMethod.Visa_Master){
                binding.btnSummary.isEnabled = false
                fetchInitData(checkoutVM.totalPrice.value!!.toDouble())
            }
        })

        binding.tvItemNum.text = "${checkoutVM.getAllProducts().size} ${getString(R.string.items)}"

        checkoutVM.deliveryMethod.observe(viewLifecycleOwner, {
            binding.tvDeliveryStatue.text = it.name.replace("_", " ")
        })

        checkoutVM.paymentMethod.observe(viewLifecycleOwner, {
            if(it == PaymentMethod.Visa_Master && checkoutVM.isAllProductsReady.value!!){
                binding.btnSummary.isEnabled = false
                fetchInitData(checkoutVM.totalPrice.value!!.toDouble())
            }
            binding.tvPaymentStatue.text = it.name.replace("_", "/")
        })

        checkoutVM.isAllProductsReady.observe(viewLifecycleOwner, {
            if(it){
                if(checkoutVM.paymentMethod.value == PaymentMethod.Visa_Master) {
                    //binding.btnSummary.isEnabled = false
                    fetchInitData(checkoutVM.totalPrice.value!!.toDouble())
                }else{
                    binding.btnSummary.isEnabled = true
                }
            }
        })

//        binding.tvSubtotalPrice.text = "${checkout.subTotalPrice} EGP"
//        binding.tvDiscountPrice.text = "${checkout.totalDiscount} EGP"
//        binding.tvShippingPrice.text = "${checkout.shipping} EGP"
//        binding.tvTotalPrice.text = "${checkout.totalPrice} EGP"

        binding.btnSummary.setOnClickListener {
            if(checkoutVM.paymentMethod.value == PaymentMethod.Visa_Master){
                presentPaymentSheet()
            }
            else{
                execOrders()
            }
            //startActivity(Intent(context,HomeActivity::class.java))
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configPaymentMethod()
    }
    
    private fun configPaymentMethod(){
        PaymentConfiguration.init(requireContext(), STRIPE_PUBLISHABLE_KEY)

        paymentSheet = PaymentSheet(this) { result ->
            onPaymentSheetResult(result)
        }

    }

    private fun execOrders(){
        binding.btnSummary.isEnabled = false
        val products = checkoutVM.getAllProducts()
        for (product in products) {
            val order = Order(product)
            order.deliveryLoc = checkoutVM.deliveryLocation.value
            order.deliveryAddress = checkoutVM.deliveryAddress.value!!
            order.deliveryMethod = checkoutVM.deliveryMethod.value!!.name.replace("_", " ")
            order.paymentMethod = checkoutVM.paymentMethod.value!!.name.replace("_", "/")
            FirebaseReferences.ordersRef.document(order.orderID).set(order)
                .addOnSuccessListener {
                    deleteFromCart(product.productID)
                    if (product == products.last()) {
                        Toast.makeText(context, getString(R.string.Success), Toast.LENGTH_SHORT)
                            .show()
                        requireActivity().finish()
                    }
                }
        }
    }

    private fun deleteFromCart(productID: String){
        if(UserInfo.userID != null) {
            val repo = FavoriteCartRepo(ShopLexDataBase.getDatabase(requireContext()).shoplexDao())
            lifecycleScope.launch {
                repo.deleteCart(productID)
                FirebaseReferences.usersRef.document(UserInfo.userID!!)
                    .collection("Lists")
                    .document("Cart")
                    .update("cartList", FieldValue.arrayRemove(productID))
            }

        }
    }


    // Payment
    private fun fetchInitData(price: Double) {
        val amount: Double = price * 100

        val payMap: MutableMap<String, Any> = HashMap()
        val itemMap: MutableMap<String, Any> = HashMap()
        val itemList: MutableList<Map<String, Any>> = ArrayList()
        payMap["currency"] = "egp"

        // itemMap["id"] = "photo_subscription"
        itemMap["amount"] = amount
        itemList.add(itemMap)
        payMap["items"] = itemList


        val mediaType = "application/json; charset=utf-8".toMediaType()
        val json = Gson().toJson(payMap)
        val body = json.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(BACKEND_URL + "checkout")
            .post(body)
            .build()

        OkHttpClient().newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(), "Not Success", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        }
                        val responseData = response.body?.string()
                        val responseJson =
                            responseData?.let { JSONObject(it) } ?: JSONObject()


                        //customerId = responseJson.getString("customer")
                        //ephemeralKeySecret = responseJson.getString("ephemeralKey")
                        paymentIntentClientSecret = responseJson.getString("clientSecret")

                        requireActivity().runOnUiThread {
                            binding.btnSummary.isEnabled = true
                        }
                    }
                }
            }
            )
    }

    private fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret
        )
    }

    private fun onPaymentSheetResult(
        paymentSheetResult: PaymentSheetResult
    ) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(
                    requireContext(),
                    "Payment Canceled",
                    Toast.LENGTH_LONG
                ).show()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(
                    requireContext(),
                    "Payment Failed. See logcat for details.",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("App", "Got error: ${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Completed -> {
                execOrders()

                //requireActivity().finish()
                Toast.makeText(
                    requireContext(),
                    "Payment Complete",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private companion object {
        private const val BACKEND_URL = "https://evening-sands-34009.herokuapp.com/"
        private const val STRIPE_PUBLISHABLE_KEY = "pk_test_51IzX9KFY0dskT72W2vHiMNJU0OGs9DukriXP1pfarCuYGkGPvZ8TaMRxxOK2W3WfQa1zO7JEOpiSqRya9BIn6okK00AZ4bRvHz"
    }

}