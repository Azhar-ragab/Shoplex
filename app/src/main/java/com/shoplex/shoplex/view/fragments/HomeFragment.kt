package com.shoplex.shoplex.view.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.chip.Chip
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentHomeBinding
import com.shoplex.shoplex.model.adapter.AdvertisementsAdapter
import com.shoplex.shoplex.model.adapter.HomeAdapter
import com.shoplex.shoplex.model.enumurations.Category
import com.shoplex.shoplex.model.enumurations.LocationAction
import com.shoplex.shoplex.model.extra.ArchLifecycleApp
import com.shoplex.shoplex.model.interfaces.FavouriteCartListener
import com.shoplex.shoplex.model.pojo.*
import com.shoplex.shoplex.room.viewmodel.CartViewModel
import com.shoplex.shoplex.view.activities.FilterActivity
import com.shoplex.shoplex.view.activities.MapsActivity
import com.shoplex.shoplex.viewmodel.ProductsVM
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter


class HomeFragment : Fragment(), FavouriteCartListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var startActivityLaunch: ActivityResultLauncher<Intent>
    private lateinit var advertisementsAdapter: AdvertisementsAdapter
    private lateinit var homeProductAdapter: HomeAdapter
    private lateinit var productsVM: ProductsVM
    private var selectedCategory: String = Category.Fashion.name
    private lateinit var cartVM: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivityLaunch =
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btnFilter.setOnClickListener {

            startActivityLaunch.launch(Intent(context, FilterActivity::class.java).apply {
                this.putExtra(FilterActivity.FILTER, productsVM.filter.value)
                this.putExtra(FilterActivity.SORT, productsVM.sort.value)
                this.putExtra(FilterActivity.SELECTED_ITEM, selectedCategory)
            })
        }

        this.productsVM = ProductsVM()
        for ((index, _) in productsVM.getCategories().withIndex()) {
            val chip = inflater.inflate(R.layout.chip_choice_item, null, false) as Chip
            chip.text = resources.getStringArray(R.array.categories)[index]
            chip.id = index
            chip.chipIcon = when (index) {
                0 -> ContextCompat.getDrawable(requireActivity(), R.drawable.fashion_chip)
                1 -> ContextCompat.getDrawable(requireActivity(), R.drawable.beauty)
                2 -> ContextCompat.getDrawable(requireActivity(), R.drawable.smartphone)
                3 -> ContextCompat.getDrawable(requireActivity(), R.drawable.electronics)
                4 -> ContextCompat.getDrawable(requireActivity(), R.drawable.accessories)
                else -> ContextCompat.getDrawable(requireActivity(), R.drawable.book)
            }
            binding.chipGroup.addView(chip)
        }

        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedCategory = group.findViewById<Chip>(checkedId).text.toString()
            val category =
                Category.values()[checkedId].name.replace(" ", getString(R.string.underscore))

            productsVM.getAllProducts(Category.valueOf(category), Filter(), null)
        }

        binding.chipGroup.findViewById<Chip>(binding.chipGroup.children.first().id).isChecked = true

        productsVM.getAllPremiums()
        productsVM.advertisements.observe(viewLifecycleOwner, { advertisements ->
            advertisementsAdapter = AdvertisementsAdapter(advertisements)
            binding.rvAdvertisement.adapter =
                ScaleInAnimationAdapter(SlideInLeftAnimationAdapter(advertisementsAdapter)).apply {
                    setDuration(1000)
                    setInterpolator(OvershootInterpolator(2f))
                    setFirstOnly(false)
                }
            //binding.rvAdvertisement.adapter = advertisementsAdapter
        })

        // Products
        cartVM = ViewModelProvider(this).get(CartViewModel::class.java)
        binding.rvHomeproducts.layoutManager =
            GridLayoutManager(this.context, getGridColumnsCount())

        productsVM.products.observe(viewLifecycleOwner, { products ->
            homeProductAdapter = HomeAdapter(products)
            binding.rvHomeproducts.adapter =
                ScaleInAnimationAdapter(SlideInBottomAnimationAdapter(homeProductAdapter)).apply {
                    setDuration(1000)
                    setInterpolator(OvershootInterpolator(2f))
                    setFirstOnly(false)
                }
            // binding.rvHomeproducts.adapter = homeProductAdapter
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty() && this@HomeFragment::homeProductAdapter.isInitialized) {
                    homeProductAdapter.search("")
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (this@HomeFragment::homeProductAdapter.isInitialized)
                    homeProductAdapter.search(binding.searchView.query.toString())
                return false
            }
        })

        binding.btnLocation.setOnClickListener {
            if (ArchLifecycleApp.isInternetConnected) {
                startActivity(
                    Intent(requireContext(), MapsActivity::class.java)
                        .apply {
                            putExtra(MapsActivity.LOCATION_ACTION, LocationAction.ShowStores.name)
                            val locations: ArrayList<LatLng> =
                                homeProductAdapter.productsHome.groupBy {
                                    it.storeLocation
                                }.map {
                                    LatLng(it.key.latitude, it.key.longitude)
                                } as ArrayList<LatLng>
                            putParcelableArrayListExtra(MapsActivity.STORE_LOCATIONS, locations)
                        }
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.NoInternetConnection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return binding.root
    }

    private fun getGridColumnsCount(): Int {
        val displayMetrics = requireContext().resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        val scalingFactor = 200
        val columnCount = (dpWidth / scalingFactor).toInt()
        return if (columnCount >= 2) columnCount else 2
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

    override fun onAddToCart(productCart: ProductCart) {
        cartVM.addCart(productCart)
    }
}