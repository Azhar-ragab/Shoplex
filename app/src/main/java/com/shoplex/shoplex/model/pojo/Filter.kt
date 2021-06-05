package com.shoplex.shoplex.model.pojo

class Filter(val lowPrice: Int? = null,
             val highPrice: Int? = null,
             val subCategory: String? = null,
             val rate: Int? = null,
             val discount: Int? = null,
             val shops: ArrayList<String>? = null) {
}