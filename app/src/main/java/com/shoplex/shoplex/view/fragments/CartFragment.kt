package eg.gov.iti.shoplex.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentCartBinding
import com.shoplex.shoplex.databinding.FragmentSummaryBinding
import com.shoplex.shoplex.model.adapter.CartAdapter
import com.shoplex.shoplex.model.adapter.SummaryAdapter
import com.shoplex.shoplex.model.pojo.Summary_Checkout
import java.util.ArrayList


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartFragment: CartAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val cart = ArrayList<Product>()
        // Inflate the layout for this fragment
        binding= FragmentCartBinding.inflate(inflater,container,false)
        cart.add(
            Product(
                "T-Shirt",
                45F,
                "Fashion",
                5,
                "https://www.5wpr.com/new/wp-content/uploads/2016/04/fashion-public-relations.jpg"

            )
        )
        cartFragment = CartAdapter(cart)


        binding.rvCart.adapter = cartFragment
        return binding.root
    }


}