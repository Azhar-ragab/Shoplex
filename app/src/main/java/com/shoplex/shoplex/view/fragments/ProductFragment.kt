package com.shoplex.shoplex

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel

import com.shoplex.shoplex.databinding.FragmentProductBinding
import java.util.*
import kotlin.collections.ArrayList


class ProductFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var binding: FragmentProductBinding
    private lateinit var propertyAdapter: PropertyAdapter

    val imageList = ArrayList<SlideModel>() // Create image list

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(inflater, container, false)

        //imgSlider
        imageList.add(SlideModel("https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70"))
        imageList.add(SlideModel("https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70"))
        imageList.add(SlideModel("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))

        binding.imgSliderDetails.setImageList(imageList, ScaleTypes.CENTER_CROP)

        //property recycler
        val property = ArrayList<Property>()
        property.add(Property("select Size", arrayListOf("37", "38", "39", "40", "41")))
        propertyAdapter = context?.let { PropertyAdapter(property, it) }!!
        binding.rvProperty.adapter = propertyAdapter
        return binding.root
    }


}