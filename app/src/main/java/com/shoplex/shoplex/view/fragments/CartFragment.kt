package eg.gov.iti.shoplex.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentCartBinding
import com.shoplex.shoplex.model.enumurations.Category
import com.shoplex.shoplex.model.enumurations.DiscountType
import com.shoplex.shoplex.model.pojo.Checkout
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.SpecialDiscount
import com.shoplex.shoplex.view.activities.CheckOutActivity


class CartFragment : Fragment() {

    lateinit var binding: FragmentCartBinding
    var productCart : ArrayList<ProductCart> = arrayListOf()
    var checkout: Checkout = Checkout()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)

       /* binding.btnCheckout.setOnClickListener {
            val product1 = Product(
                "T-Shirt",
                30F,
                "Fashion",
                LatLng(0.0, 0.0),
                "https://static.zara.net/photos///2021/V/0/1/p/4424/156/800/2/w/167/4424156800_6_1_1.jpg?ts=1612858156056"
            )
            val product2 = Product(
                "Shoes",
                100F,
                "Fashion",
                LatLng(0.0, 0.0),
                "https://static.zara.net/photos///2021/V/0/1/p/4424/156/800/2/w/167/4424156800_6_1_1.jpg?ts=1612858156056"
            )
            val product3 = Product(
                "Deress",
                500F,
                "Fashion",
                LatLng(0.0, 0.0),
                "https://static.zara.net/photos///2021/V/0/1/p/4424/156/800/2/w/167/4424156800_6_1_1.jpg?ts=1612858156056"
            )
            productCart.add(ProductCart(product1, 1, SpecialDiscount(5f, DiscountType.Fixed)))
            productCart.add(ProductCart(product2, 4, SpecialDiscount(10f, DiscountType.Percentage)))
            productCart.add( ProductCart(product3, 1, SpecialDiscount(5f, DiscountType.Fixed)))

            for (item in productCart){
                checkout.addProduct(item)
            }
            var intent = Intent(context, CheckOutActivity::class.java)
            intent.putParcelableArrayListExtra("checkout",productCart)
            startActivity(intent)

        }*/
        return binding.root
    }

}