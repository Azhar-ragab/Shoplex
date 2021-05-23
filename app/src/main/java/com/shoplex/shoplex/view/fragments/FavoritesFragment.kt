package eg.gov.iti.shoplex.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.databinding.FragmentFavoritesBinding
import com.shoplex.shoplex.model.adapter.FavouriteAdapter
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.pojo.User
import kotlin.collections.ArrayList

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favouriteAdapter: FavouriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)


        var favouriteList = ArrayList<String>()
        var favouriteProducts =ArrayList<Product>()
        FirebaseReferences.userRef.whereEqualTo(
            "email",
            Firebase.auth.currentUser.email
        ).get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.exists()) {
                    val u = document.toObject<User>()
                    FirebaseReferences.userRef.document(u.userID).get()
                        .addOnSuccessListener { favourite ->
                            if (favourite != null) {
                                val user = favourite.toObject<User>()
                                favouriteList = user!!.favouriteList
                                for (product in favouriteList) {
                                    FirebaseReferences.productsRef.document(product).get()
                                        .addOnSuccessListener { productResult ->
                                            if (productResult != null) {
                                                 val prod = productResult.toObject<Product>()
                                                favouriteProducts.add(prod!!)
                                                if (document.equals(result.last())) {
                                                    favouriteAdapter = FavouriteAdapter(favouriteProducts)
                                                     binding.rvFavourite.adapter = favouriteAdapter
                                                }
                                            }
                                        }
                                }
//                        Toast.makeText(context,favouriteList[0],Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }

        return binding.root
    }


}