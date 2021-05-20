package com.shoplex.shoplex

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.shoplex.shoplex.databinding.FragmentProductBinding
import com.shoplex.shoplex.model.adapter.ChatHeadAdapter
import com.shoplex.shoplex.model.pojo.Store
import com.shoplex.shoplex.view.activities.MapsActivity
import com.shoplex.shoplex.view.activities.MessageActivity
import com.shoplex.shoplex.view.activities.ProductDetails
import com.shoplex.shoplex.viewmodel.DetailsVM
import com.shoplex.shoplex.viewmodel.ProductsVM
import eg.gov.iti.shoplex.fragments.ChatFragment


class ProductFragment(val productId: String) : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var binding: FragmentProductBinding
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var productsVM: ProductsVM
    private lateinit var product: Product
    private lateinit var detailsVM: DetailsVM
    private lateinit var storeInfo: Store
    private val imageList = ArrayList<SlideModel>() // Create image list
    private val CHAT_TITLE_KEY = "CHAT_TITLE_KEY"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(inflater, container, false)
        this.productsVM = ProductsVM()
        this.detailsVM = DetailsVM()
        productsVM.getProductById(productId)
        productsVM.products.observe(this.viewLifecycleOwner, Observer {
            if (it.count() > 0) {
                this.product = it[0]
                detailsVM.getStoreData(product.storeID)
                binding.product = product
                for (img in product.images)
                    imageList.add(SlideModel(img))
                binding.imgSliderDetails.setImageList(imageList, ScaleTypes.CENTER_CROP)
                propertyAdapter = context?.let { PropertyAdapter(product.properties, it) }!!
                binding.rvProperty.adapter = propertyAdapter


            }

        })
        detailsVM.store.observe(this.viewLifecycleOwner, Observer {
            this.storeInfo=it
        })
        binding.btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse("tel:${storeInfo.phone}")
            startActivity(intent)

        }
        binding.btnMessage.setOnClickListener {
            var intent: Intent = Intent(binding.root.context, MessageActivity::class.java)
            intent.putExtra("chatID", storeInfo.storeID)
            intent.putExtra(CHAT_TITLE_KEY, product.storeName)
            intent.putExtra("phoneNumber",storeInfo.phone)
            binding.root.context.startActivity(intent)


        }


//        Toast.makeText(context,productId,Toast.LENGTH_SHORT).show()

        //imgSlider
//        imageList.add(SlideModel("https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70"))
//        imageList.add(SlideModel("https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70"))
//        imageList.add(SlideModel("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))

        //property recycler
//        val property = ArrayList<Property>()
//        property.add(Property("select Size", arrayListOf("37", "38", "39", "40", "41")))


        return binding.root
    }


}