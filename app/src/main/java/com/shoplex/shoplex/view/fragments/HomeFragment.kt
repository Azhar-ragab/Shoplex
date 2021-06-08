package com.shoplex.shoplex.view.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentHomeBinding
import com.shoplex.shoplex.model.adapter.AdvertisementsAdapter
import com.shoplex.shoplex.model.adapter.HomeProductsAdapter
import com.shoplex.shoplex.model.enumurations.Category
import com.shoplex.shoplex.model.pojo.Filter
import com.shoplex.shoplex.model.pojo.Sort
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.ProductFavourite
import com.shoplex.shoplex.room.Lisitener
import com.shoplex.shoplex.room.viewmodel.CartViewModel
import com.shoplex.shoplex.room.viewmodel.FavouriteViewModel
import com.shoplex.shoplex.view.activities.FilterActivity
import com.shoplex.shoplex.viewmodel.ProductsVM


class HomeFragment : Fragment(), Lisitener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var startActivitylaunch: ActivityResultLauncher<Intent>
    private lateinit var advertisementsAdapter: AdvertisementsAdapter
    private lateinit var homeProductAdapter: HomeProductsAdapter
    private lateinit var productsVM: ProductsVM
    private var selectedCategory: String = Category.Fashion.name
    private lateinit var cartVM: CartViewModel
    private lateinit var favouriteViewModel: FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivitylaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = it.data
                    if (data != null)
                        startFilter(data)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btnFilter.setOnClickListener {

            startActivitylaunch.launch(Intent(context, FilterActivity::class.java).apply {
                this.putExtra(FilterActivity.CATEGORY, selectedCategory)
            })
        }

        this.productsVM = ProductsVM()
        for ((index, cat) in productsVM.getCategories().withIndex()) {
            val chip = inflater.inflate(R.layout.chip_choice_item, null, false) as Chip
            chip.text = cat
            chip.id = index
            binding.chipGroup.addView(chip)
        }

        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedCategory = group.findViewById<Chip>(checkedId).text.toString()
            val category =
                Category.valueOf(selectedCategory.replace(" ", getString(R.string.underscore)))

            /*
            val shops = arrayListOf("b4a2643b-7dba-4c29-9bf1-e53dd73acc3b", "b31eafa4-8167-4ee0-92de-6fb5d3b1c0ef")
            val filter = Filter(lowPrice = 50, highPrice = 250, subCategory = null, rate = null, discount = null, shops = null)
            val sort = Sort(price = null, rate = false, discount = true, nearestShop = false)
*/
            productsVM.getAllProducts(category, Filter(), null)
        }

        binding.chipGroup.findViewById<Chip>(binding.chipGroup.children.first().id).isChecked = true

        productsVM.getAllPremiums()
        productsVM.advertisments.observe(viewLifecycleOwner, { advertisements ->
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
        cartVM = ViewModelProvider(this).get(CartViewModel::class.java)
        favouriteViewModel=ViewModelProvider(this).get(FavouriteViewModel::class.java)
        binding.rvHomeproducts.layoutManager =
            GridLayoutManager(this.context, getGridColumnsCount())

        productsVM.products.observe(viewLifecycleOwner, Observer { products ->
            homeProductAdapter = HomeProductsAdapter(products, this,this)
            binding.rvHomeproducts.adapter = homeProductAdapter
        })

        return binding.root
    }

    private fun getGridColumnsCount(): Int {
        val displayMetrics = requireContext().resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        val scalingFactor = 200 // You can vary the value held by the scalingFactor
        val columnCount = (dpWidth / scalingFactor).toInt()
        return if (columnCount >= 2) columnCount else 2 // if column no. is less than 2, we still display 2 columns
    }

    private fun startFilter(data: Intent) {
        val filter: Parcelable? = data.getParcelableExtra(FilterActivity.FILTER)
        val sort: Parcelable? = data.getParcelableExtra(FilterActivity.SORT)

        val userFilter: Filter = filter as Filter
        val userSort = sort as? Sort
        val category =
            Category.valueOf(selectedCategory.replace(" ", getString(R.string.underscore)))
        productsVM.getAllProducts(category, userFilter, userSort)
    }

    override fun onaddCart(productCart: ProductCart) {
        cartVM.addCart(productCart)
    }

    override fun onaddFavourite(productFavourite: ProductFavourite) {
        favouriteViewModel.addFavourite(productFavourite)
    }



}