package com.shoplex.shoplex.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityFilterBinding
import java.text.NumberFormat
import java.util.*
import com.shoplex.shoplex.databinding.BottomSheetShopsBinding
import com.shoplex.shoplex.model.adapter.StoresLocationsAdapter
import com.shoplex.shoplex.model.adapter.SubCategoryAdapter
import com.shoplex.shoplex.model.enumurations.*
import com.shoplex.shoplex.model.pojo.Filter
import com.shoplex.shoplex.model.pojo.Sort
import com.shoplex.shoplex.model.pojo.StoreLocationInfo
import com.shoplex.shoplex.viewmodel.StoresVM
import kotlinx.android.synthetic.main.activity_filter.*
import kotlin.collections.ArrayList


class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding
    //private var subCatCheckList: ArrayList<String> = arrayListOf()
    //private var selectedItem = Category.Fashion.name

    private lateinit var storesVM: StoresVM

    companion object{
        val CATEGORY = "CATEGORY"
        val FILTER = "FILTER"
        val SORT = "SORT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBarFilter)

        storesVM = ViewModelProvider(this).get(StoresVM::class.java)

        storesVM.selectedItem.value = intent.getStringExtra(CATEGORY).toString()

        supportActionBar?.apply {
            title = getString(R.string.filter)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }

        //format range slider Label
        binding.rsPrice.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance(getString(R.string.EGP))
            format.format(value.toDouble())
        }


        binding.cardCategoryFilter.setOnClickListener {
            subCategoryBottomSheetDialog()
        }

        binding.cardShopesFilter.setOnClickListener {
            shopsBottomSheetDialog()
        }

        binding.btnFilterOK.setOnClickListener {
            var filter = Filter()
            var sort: Sort? = null


            var stores: ArrayList<String>? = storesVM.storesList.value
            var subCategory: ArrayList<String>? = storesVM.subCatCheckList.value
            var minPrice: Int? = null
            var maxPrice: Int? = null
            var rateFilter: Float? = null
            var discountFilter: Int? = null

            if (stores.isNullOrEmpty())
                stores = null

            if (subCategory.isNullOrEmpty())
                subCategory = null

            if (binding.cbPriceFilter.isChecked) {
                minPrice = binding.rsPrice.values[0].toInt()
                maxPrice = binding.rsPrice.values[1].toInt()
            }

            if (binding.cbRateFilter.isChecked)
                rateFilter = binding.ratingBarFilter.rating

            if (binding.cbDiscountFilter.isChecked)
                discountFilter = binding.sliderDiscount.value.toInt()

            filter = Filter(
                lowPrice = minPrice,
                highPrice = maxPrice,
                subCategory = subCategory,
                rate = rateFilter,
                discount = discountFilter,
                shops = stores
            )

            var price = binding.cbPrice.isChecked
            val rate = binding.cbRating.isChecked
            val discount = binding.cbDiscount.isChecked
            val nearestShop = binding.cbNersedtShop.isChecked

            if (price || rate || discount || nearestShop) {
                price = (binding.toggleBtnPrice.checkedButtonId != binding.btnLowPrice.id)
                sort = Sort(price, rate, discount, nearestShop)
            }

            setResult(RESULT_OK, Intent().apply {
                putExtra(FILTER, filter)
                putExtra(SORT, sort)
            })

            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shopsBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this,R.style.BottomSheetDialogTheme)

        val bottomSheetShopsBinding = BottomSheetShopsBinding.inflate(layoutInflater)
        var storesLocations: ArrayList<StoreLocationInfo> = arrayListOf()

        storesVM.getStoresInfo()

        storesVM.storesLocationInfo.observe(this, {
            val adapter: StoresLocationsAdapter = StoresLocationsAdapter(it, storesVM.storesList.value!!)
            bottomSheetShopsBinding.rvShops.adapter = adapter
        })

        bottomSheetDialog.setContentView(bottomSheetShopsBinding.root)

        bottomSheetDialog.show()
    }

    private fun subCategoryBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this,R.style.BottomSheetDialogTheme)
        val bottomSheetShopsBinding = BottomSheetShopsBinding.inflate(layoutInflater)
        val adapter: SubCategoryAdapter = SubCategoryAdapter(getSubCategory(storesVM.selectedItem.value!!), storesVM.subCatCheckList.value!!)
        bottomSheetShopsBinding.rvShops.adapter = adapter

        bottomSheetDialog.setContentView(bottomSheetShopsBinding.root)

        bottomSheetDialog.show()
    }

    fun getSubCategory(selectedItem: String): Array<String>{
        val listSubCat =
            when(Category.valueOf(selectedItem.replace(" ", "_"))) {
                Category.Fashion -> SubFashion.values()
                Category.Health_Care -> SubHealth.values()
                Category.Phone_and_Tablets -> SubPhone.values()
                Category.Electronics -> SubElectronic.values()
                Category.Accessories -> SubAccessors.values()
                Category.Books -> SubBook.values()
            }

        return ((listSubCat as Array<*>).map {
            it.toString().split("_").joinToString(" ")
        }.toTypedArray())
    }


}