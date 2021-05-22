package eg.gov.iti.shoplex.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.View.generateViewId
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.generateViewId
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.firebase.installations.Utils
import com.google.type.LatLng
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentHomeBinding
import com.shoplex.shoplex.model.adapter.AdvertisementAdapter
import com.shoplex.shoplex.model.adapter.HomeProductsAdapter
import com.shoplex.shoplex.model.enumurations.Category
import com.shoplex.shoplex.model.pojo.Ads_Home
import com.shoplex.shoplex.model.pojo.Products_Home
import com.shoplex.shoplex.view.activities.FilterActivity
import com.shoplex.shoplex.viewmodel.ProductsVM


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var advertisementAdapter: AdvertisementAdapter
    private lateinit var homeProductAdapter: HomeProductsAdapter
    private lateinit var productsVM: ProductsVM
    private val FILTER_CODE = 202

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btnFilter.setOnClickListener {

            startActivityForResult(Intent(context, FilterActivity::class.java), FILTER_CODE)

        }
        this.productsVM = ProductsVM()
        for ((index, cat) in productsVM.getCategories().withIndex()){
            val chip = inflater.inflate(R.layout.chip_choice_item, null, false) as Chip
            chip.text = cat
            chip.id = index
            binding.chipGroup.addView(chip)
        }

        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val category = Category.valueOf(group.findViewById<Chip>(checkedId).text.toString().replace(" ", "_"))
            productsVM.getAllProducts(category)
        }
/*
        val advertisement = ArrayList<Ads_Home>()

        advertisement.add(
            Ads_Home(
                "T-Shirt",
                "https://www.5wpr.com/new/wp-content/uploads/2016/04/fashion-public-relations.jpg",
                "Offer 25%"
            )
        )
        advertisement.add(
            Ads_Home(
                "Pants",
                "https://cdn.shopify.com/s/files/1/0089/3989/6947/files/header-2.3_2e9bf8b4-a065-4aea-9beb-c6913d0344b9_800x.jpg?v=1618672152",
                "Offer 25%"
            )
        )
        advertisement.add(
            Ads_Home(
                "Pants",
                "https://images.unsplash.com/photo-1483985988355-763728e1935b?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8ZmFzaGlvbnxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=1000&q=80",
                "Offer 25%"
            )
        )
        */
//        productsVM.products.observe(viewLifecycleOwner, Observer{ advertisements ->
//            advertisementAdapter = AdvertisementAdapter(advertisements)
//            binding.rvAdvertisement.adapter = advertisementAdapter
//        })

        // Products

        /*
        products.add(
            Products_Home("Sport Dress" ,
                12F, 10.5F , 4.5,"Heba" ,"Active Store","5Km/m","https://cdn.shopify.com/s/files/1/0089/3989/6947/files/header-2.3_2e9bf8b4-a065-4aea-9beb-c6913d0344b9_800x.jpg?v=1618672152",5

            )
        )
        products.add(
            Products_Home("Sport Dress" ,
                12F, 10.5F , 4.5,"Heba" ,"Swich Store","5Km/m","https://images.unsplash.com/photo-1483985988355-763728e1935b?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8ZmFzaGlvbnxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=1000&q=80",5

            )
        )
        products.add(
            Products_Home("Sport Dress" ,
                12F, 10.5F , 4.5,"Heba" ,"Active Store","3Km/m ","https://images.unsplash.com/photo-1483985988355-763728e1935b?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8ZmFzaGlvbnxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=1000&q=80,5",5

            )
        )
        */

        binding.rvHomeproducts.layoutManager = GridLayoutManager(this.context, getGridColumnsCount())
        productsVM.products.observe(viewLifecycleOwner, Observer{ products ->
            homeProductAdapter = HomeProductsAdapter(products)
            binding.rvHomeproducts.adapter = homeProductAdapter
        })

        return binding.root
    }

    fun getGridColumnsCount(): Int {
        val displayMetrics = requireContext().resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        val scalingFactor = 200 // You can vary the value held by the scalingFactor
        val columnCount = (dpWidth / scalingFactor).toInt()
        return if (columnCount >= 2) columnCount else 2 // if column no. is less than 2, we still display 2 columns
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == FILTER_CODE){
            if(resultCode == AppCompatActivity.RESULT_OK){

            }
        }
    }
}