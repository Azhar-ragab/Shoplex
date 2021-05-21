package com.shoplex.shoplex.model.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter



import com.shoplex.shoplex.view.fragments.DeliveryFragment
import com.shoplex.shoplex.view.fragments.PaymentFragment
import com.shoplex.shoplex.view.fragments.SummaryFragment

class CheckoutAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager)  {

     override fun getCount(): Int {
        return 3
    }

     override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                DeliveryFragment()
            }
            1 -> {
                PaymentFragment()
            }
            else -> {
                SummaryFragment()
            }

        }
    }
     override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Delivery"
            }
            1->{
                return "Payment"
            }
            else-> {
                return "Summary"
            }

        }
    }

}