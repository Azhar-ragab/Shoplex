package com.shoplex.shoplex

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fm:FragmentManager,val productId:String) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return ProductFragment(productId)
            }
            else -> {
                return ReviewFragment(productId)
            }
        }
    }
    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Products"
            }
            else -> {
                return "Reviews"
            }
        }
    }


}