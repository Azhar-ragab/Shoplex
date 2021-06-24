package com.shoplex.shoplex.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityFilterBinding
import com.shoplex.shoplex.databinding.BottomSheetShopsBinding
import com.shoplex.shoplex.model.adapter.StoresLocationsAdapter
import com.shoplex.shoplex.model.adapter.SubCategoryAdapter
import com.shoplex.shoplex.model.enumurations.*
import com.shoplex.shoplex.model.pojo.Filter
import com.shoplex.shoplex.model.pojo.Sort
import com.shoplex.shoplex.model.pojo.StoreLocationInfo
import com.shoplex.shoplex.viewmodel.StoresVM
import com.shoplex.shoplex.viewmodel.StoresVMFactory
import java.text.NumberFormat
import java.util.*

class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding
    private lateinit var storesVM: StoresVM
    private var filter: Filter? = null
    private var sort: Sort? = null

    companion object {
        const val SELECTED_ITEM = "SELECTED_ITEM"
        const val FILTER = "FILTER"
        const val SORT = "SORT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBarFilter)

        val category = intent.getStringExtra(SELECTED_ITEM) ?: Category.Fashion.name
        filter = intent.getParcelableExtra(FILTER)
        sort = intent.getParcelableExtra(SORT)

        storesVM =
            ViewModelProvider(this, StoresVMFactory(this, category)).get(StoresVM::class.java)

        if (filter?.subCategory != null)
            storesVM.subCatCheckList.value = filter?.subCategory!!

        if (filter?.shops != null)
            storesVM.storesList.value = filter?.shops!!

        if (filter?.rate != null) {
            binding.cbRateFilter.isChecked = true
            binding.ratingBarFilter.rating = filter?.rate!!
        }

        if (filter?.lowPrice != null) {
            binding.cbPriceFilter.isChecked = true
            binding.rsPrice.values =
                listOf(filter?.lowPrice!!.toFloat(), filter?.highPrice!!.toFloat())
        }

        if (filter?.discount != null) {
            binding.cbDiscountFilter.isChecked = true
            binding.sliderDiscount.value = filter?.discount!!.toFloat()
        }

        binding.btnLowPrice.isCheckable = true

        if (sort != null) {
            if (sort?.price == true) {
                binding.cbPrice.isChecked = true
                binding.btnHighPrice.isChecked = true
            } else if (sort?.price == false) {
                binding.cbPrice.isChecked = true
                binding.btnLowPrice.isChecked = true
            }

            binding.cbRating.isChecked = sort!!.rate
            binding.cbDiscount.isChecked = sort!!.discount
            binding.cbNersedtShop.isChecked = sort!!.nearestShop
        }

        supportActionBar?.apply {
            title = getString(R.string.filter)
           // setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        binding.rsPrice.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance(getString(R.string.StrEGP))
            format.format(value.toDouble())
        }


        binding.cardCategoryFilter.setOnClickListener {
            subCategoryBottomSheetDialog()
        }

        binding.cardShopesFilter.setOnClickListener {
            if (!storesVM.storesLocationInfo.value.isNullOrEmpty()) {
                shopsBottomSheetDialog(storesVM.storesLocationInfo.value!!)
            } else {
                val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.no_store), Snackbar.LENGTH_LONG)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
                snackbar.show()
            }
        }

        binding.cbPriceFilter.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                binding.cbPrice.isChecked = true
                binding.cbPrice.isClickable = false
            } else {
                binding.cbPrice.isChecked = false
                binding.cbPrice.isClickable = true
            }
        }

        binding.btnFilterOK.setOnClickListener {
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

            var price: Boolean? = binding.cbPrice.isChecked
            val rate = binding.cbRating.isChecked
            val discount = binding.cbDiscount.isChecked
            val nearestShop = binding.cbNersedtShop.isChecked

            if(price != true)
                price = null

            if (price == true || rate || discount || nearestShop) {
                price = if(price == true)
                    (binding.toggleBtnPrice.checkedButtonId != binding.btnLowPrice.id)
                else
                    null
                sort = Sort(price, rate, discount, nearestShop)
            } else {
                sort = null
            }

            setResult(RESULT_OK, Intent().apply {
                putExtra(FILTER, filter)
                putExtra(SORT, sort)
            })

            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()

        return super.onOptionsItemSelected(item)
    }

    private fun shopsBottomSheetDialog(storesLocations: ArrayList<StoreLocationInfo>) {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)

        val bottomSheetShopsBinding = BottomSheetShopsBinding.inflate(layoutInflater)
        bottomSheetShopsBinding.rvShops.adapter =
            StoresLocationsAdapter(storesLocations, storesVM.storesList.value!!)

        bottomSheetDialog.setContentView(bottomSheetShopsBinding.root)
        bottomSheetDialog.show()
    }

    private fun subCategoryBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val bottomSheetShopsBinding = BottomSheetShopsBinding.inflate(layoutInflater)
        val adapter = SubCategoryAdapter(
            getSubCategory(storesVM.selectedItem.value!!),
            storesVM.subCatCheckList.value!!
        )
        bottomSheetShopsBinding.rvShops.adapter = adapter

        bottomSheetDialog.setContentView(bottomSheetShopsBinding.root)

        bottomSheetDialog.show()
    }

    private fun getSubCategory(selectedItem: String): Array<String> {
        val listSubCat =
            when (Category.valueOf(selectedItem.replace(" ", "_"))) {
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