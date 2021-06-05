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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentHomeBinding
import com.shoplex.shoplex.model.adapter.AdvertisementsAdapter
import com.shoplex.shoplex.model.adapter.HomeProductsAdapter
import com.shoplex.shoplex.model.enumurations.Category
import com.shoplex.shoplex.model.pojo.Filter
import com.shoplex.shoplex.model.pojo.Sort
import com.shoplex.shoplex.view.activities.FilterActivity
import com.shoplex.shoplex.viewmodel.ProductsVM


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var advertisementsAdapter: AdvertisementsAdapter
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
            val category = Category.valueOf(group.findViewById<Chip>(checkedId).text.toString().replace(" ", getString(R.string.underscore)))
            val shops = arrayListOf("b4a2643b-7dba-4c29-9bf1-e53dd73acc3b", "b31eafa4-8167-4ee0-92de-6fb5d3b1c0ef")
            val filter = Filter(lowPrice = 50, highPrice = 250, subCategory = null, rate = null, discount = null, shops = null)
            val sort = Sort(price = null, rate = false, discount = true, nearestShop = false)

            productsVM.getAllProducts(category, filter, sort)
        }

        binding.chipGroup.findViewById<Chip>(binding.chipGroup.children.first().id).isChecked = true

        productsVM.getAllPremiums()
        productsVM.advertisments.observe(viewLifecycleOwner, Observer{ advertisements ->
            advertisementsAdapter = AdvertisementsAdapter(advertisements)
            binding.rvAdvertisement.adapter = advertisementsAdapter
       })

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