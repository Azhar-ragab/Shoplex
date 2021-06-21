package com.shoplex.shoplex.model.pojo

import com.shoplex.shoplex.model.enumurations.DiscountType

class SpecialCoupon(
    val productName: String = "",
    discount: Float = 0F,
    discountType: DiscountType
) : SpecialDiscount(discount, discountType)