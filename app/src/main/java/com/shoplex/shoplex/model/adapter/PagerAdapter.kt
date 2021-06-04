package com.shoplex.shoplex.model.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.shoplex.shoplex.ProductFragment
import com.shoplex.shoplex.R
import com.shoplex.shoplex.view.fragments.ReviewFragment


class PagerAdapter(fm: FragmentManager, val context: Context, val productId:String) : FragmentPagerAdapter(fm) {
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
                return this.context.getString(R.string.Products)
            }
            else -> {
                return this.context.getString(R.string.Reviews)
            }
        }
    }


}