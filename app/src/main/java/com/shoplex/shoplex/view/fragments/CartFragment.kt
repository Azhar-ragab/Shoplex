package eg.gov.iti.shoplex.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.shoplex.shoplex.model.pojo.Summary_Checkout
import com.shoplex.shoplex.model.pojo.User
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


        var cartList = ArrayList<String>()
        var cartProducts = ArrayList<Product>()
        FirebaseReferences.userRef.whereEqualTo(
            "email",
            Firebase.auth.currentUser.email
        ).get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.exists()) {
                    val u = document.toObject<User>()
                    FirebaseReferences.userRef.document(u.userID).get()
                        .addOnSuccessListener { cart ->
                            if (cart != null) {
                                val user = cart.toObject<User>()
                                cartList = user!!.cartList
                                for (product in cartList) {
                                    FirebaseReferences.productsRef.document(product).get()
                                        .addOnSuccessListener { productResult ->
                                            if (productResult != null) {
                                                val prod = productResult.toObject<Product>()
                                                cartProducts.add(prod!!)
                                                if (document.equals(result.last())) {
                                                    cartAdapter = CartAdapter(cartProducts)
                                                    binding.rvCart.adapter = cartAdapter
                                                }
                                            }
                                        }
                                }

                            }
                        }
                }
            }
        }

        return binding.root
    }


}