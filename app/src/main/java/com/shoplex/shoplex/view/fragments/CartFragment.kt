package com.shoplex.shoplex.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentCartBinding
import com.shoplex.shoplex.model.adapter.CartAdapter
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.interfaces.FavouriteCartListener
import com.shoplex.shoplex.model.pojo.ProductQuantity
import com.shoplex.shoplex.room.viewmodel.CartViewModel
import com.shoplex.shoplex.view.activities.CheckOutActivity

class CartFragment : Fragment(), FavouriteCartListener {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    private var productsQuantity: ArrayList<ProductQuantity> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)

        binding.btnCheckout.setOnClickListener {
            if (UserInfo.userID != null) {
                startActivity(Intent(context, CheckOutActivity::class.java).apply {
                    this.putParcelableArrayListExtra("PRODUCTS_QUANTITY", productsQuantity)
                })
            } else {
                Toast.makeText(context, getString(R.string.validation), Toast.LENGTH_SHORT).show()
            }
        }
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        if (UserInfo.userID != null) {
            getAllCartProducts()
        } else {
            Toast.makeText(context, getString(R.string.pleaseLogin), Toast.LENGTH_SHORT).show()
        }

        return binding.root

    }

    fun getAllCartProducts() {
        val cartAdapter = CartAdapter(this)
        binding.rvCart.adapter = cartAdapter
        cartViewModel.readAllCart.observe(viewLifecycleOwner, Observer {
            cartAdapter.setData(it)
            binding.tvPrice.text = it.map { product ->
                product.cartQuantity * product.newPrice
            }.sum().toString()

            productsQuantity.clear()
            it.forEach { product ->
                productsQuantity.add(ProductQuantity(product.productID, product.cartQuantity))
            }
        })
        /*  var cartProducts = ArrayList<ProductCart>()
          FirebaseReferences.usersRef.document(UserInfo.userID!!).get()
              .addOnSuccessListener { result ->
                  val cartList: ArrayList<String> = result.get("cartList") as ArrayList<String>
                  for (productID in cartList) {
                      FirebaseReferences.productsRef.document(productID).get()
                          .addOnSuccessListener { productResult ->
                              if (productResult != null) {
                                  val prod = productResult.toObject<ProductCart>()
                                  cartProducts.add(prod!!)
                                 // cartViewModel.addCart(prod)
                                  if (productID == cartList.last()) {
                                      cartAdapter =
                                          CartAdapter(cartProducts,this,this)
                                      binding.rvCart.adapter = cartAdapter
                                  }
                              }
                          }
                  }
              }*/
    }

    override fun onDeleteFromCart(productID: String) {
        super.onDeleteFromCart(productID)
        cartViewModel.deleteCart(productID)
    }

    override fun onUpdateCartQuantity(productID: String, quantity: Int) {
        cartViewModel.updateCart(productID, quantity)
    }
}