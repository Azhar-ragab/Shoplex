package com.shoplex.shoplex.model.pojo

import android.os.Parcel
import android.os.Parcelable
import com.shoplex.shoplex.model.enumurations.DiscountType

 class SpecialDiscount : Parcelable {
    var discount: Float = 0F
    var discountType: DiscountType = DiscountType.Fixed

    constructor(parcel: Parcel) : this() {
        discount = parcel.readFloat()
    }

     constructor()
    constructor(discount: Float, discountType: DiscountType){
        this.discount = discount
        this.discountType = discountType
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<SpecialDiscount> {
        override fun createFromParcel(parcel: Parcel): SpecialDiscount {
            return SpecialDiscount(parcel)
        }

        override fun newArray(size: Int): Array<SpecialDiscount?> {
            return arrayOfNulls(size)
        }
    }
}