package com.shoplex.shoplex

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter




class PagerAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return ProductFragment()
            }
            else -> {
                return ReviewFragment()
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