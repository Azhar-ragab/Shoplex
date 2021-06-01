package eg.gov.iti.shoplex.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentCartBinding
import com.shoplex.shoplex.databinding.FragmentFavoritesBinding
import com.shoplex.shoplex.databinding.FragmentSummaryBinding
import com.shoplex.shoplex.model.adapter.CartAdapter
import com.shoplex.shoplex.model.adapter.FavouriteAdapter
import com.shoplex.shoplex.model.adapter.SummaryAdapter
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Checkout
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.Summary_Checkout
import com.shoplex.shoplex.model.pojo.User
import com.shoplex.shoplex.view.activities.CheckOutActivity
import java.util.ArrayList


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                            cartProducts.add(prod!!)
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