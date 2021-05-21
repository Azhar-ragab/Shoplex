package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shoplex.shoplex.databinding.FragmentDeliveryBinding


class DeliveryFragment : Fragment() {

 lateinit var binding: FragmentDeliveryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDeliveryBinding.inflate(inflater,container,false)


        return binding.root
    }


}