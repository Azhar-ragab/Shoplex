package com.shoplex.shoplex.model.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.shoplex.shoplex.R
import com.shoplex.shoplex.view.fragments.DetailsFragment
import com.shoplex.shoplex.view.fragments.ReviewFragment

class PagerAdapter(fm: FragmentManager, val context: Context, val productId:String) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> DetailsFragment(productId)
            else -> ReviewFragment(productId)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> this.context.getString(R.string.Products)
            else -> this.context.getString(R.string.Reviews)
        }
    }
}