package com.shoplex.shoplex.model.extra

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class CheckoutViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    var isPagingEnabled: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return isPagingEnabled
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return isPagingEnabled
    }
}