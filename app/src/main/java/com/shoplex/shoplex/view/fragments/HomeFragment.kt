package com.shoplex.shoplex.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentHomeBinding
import com.shoplex.shoplex.model.adapter.AdvertisementsAdapter
import com.shoplex.shoplex.model.adapter.HomeProductsAdapter
import com.shoplex.shoplex.model.enumurations.Category
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.room.Lisitener
import com.shoplex.shoplex.room.viewmodel.CartViewModel
import com.shoplex.shoplex.view.activities.FilterActivity
import com.shoplex.shoplex.viewmodel.ProductsVM


class HomeFragment : Fragment(),Lisitener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var advertisementsAdapter: AdvertisementsAdapter
    private lateinit var homeProductAdapter: HomeProductsAdapter
    private lateinit var productsVM: ProductsVM
    private val FILTER_CODE = 202
    private lateinit var cartVM:CartViewModel


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
            val category = Category.valueOf(group.findViewById<Chip>(checkedId).text.toString().replace(" ", getString(R.string.underscore)))
            productsVM.getAllProducts(category)
        }

        binding.chipGroup.findViewById<Chip>(binding.chipGroup.children.first().id).isChecked = true

/*
        val advertisement = ArrayList<Ads_Home>()

        advertisement.add(
            Ads_Home(
                "T-Shirt",
                "http://cdn.shopify.com/s/files/1/0002/2573/8783/products/Tees_Fashion_Blue_90f965c8-8edc-4ef8-99dd-1dcb6977ce13_1078x.png?v=1536327040",
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

        productsVM.getAllPremiums()
        productsVM.advertisments.observe(viewLifecycleOwner, Observer{ advertisements ->
            advertisementsAdapter = AdvertisementsAdapter(advertisements)
            binding.rvAdvertisement.adapter = advertisementsAdapter
       })

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
cartVM=ViewModelProvider(this).get(CartViewModel::class.java)

        binding.rvHomeproducts.layoutManager = GridLayoutManager(this.context, getGridColumnsCount())
        productsVM.products.observe(viewLifecycleOwner, Observer{ products ->
            homeProductAdapter = HomeProductsAdapter(products,this)
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

    override fun onaddCart(productCart: ProductCart) {
        cartVM.addCart(productCart)
    }


}