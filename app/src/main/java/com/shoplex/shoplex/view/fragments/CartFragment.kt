package com.shoplex.shoplex.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentCartBinding
import com.shoplex.shoplex.model.adapter.CartAdapter
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.view.activities.CheckOutActivity
import java.util.*

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)

        binding.btnCheckout.setOnClickListener {
            if(UserInfo.userID != null){
                startActivity(Intent(context, CheckOutActivity::class.java))
            }else{
                Toast.makeText(context, getString(R.string.validation), Toast.LENGTH_SHORT).show()
            }
        }

        if(UserInfo.userID != null){
            getAllCartProducts()
        }else{
            Toast.makeText(context, getString(R.string.pleaseLogin), Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    fun getAllCartProducts() {

        var cartProducts = ArrayList<ProductCart>()
        FirebaseReferences.usersRef.document(UserInfo.userID!!).get().addOnSuccessListener { result ->
            val cartList: ArrayList<String> = result.get("cartList") as ArrayList<String>
            for (productID in cartList){
                FirebaseReferences.productsRef.document(productID).get()
                    .addOnSuccessListener { productResult ->
                        if (productResult != null) {
                            val prod = productResult.toObject<ProductCart>()
                            if(prod != null)
                                cartProducts.add(prod)

                            if (productID == cartList.last()) {
                                cartAdapter =
                                    CartAdapter(cartProducts)
                                binding.rvCart.adapter = cartAdapter
                            }

                        }
                    }
            }
        }
    }
}