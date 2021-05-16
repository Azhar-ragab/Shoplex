package com.shoplex.shoplex

import android.widget.RatingBar
import java.util.*

class Review {

    var productID : Int = 0
    var customerName : String = ""
    var customerImageUrl : String = ""
    var comment : String = ""
    lateinit var date : Date
    lateinit var rate : RatingBar

    constructor(
        customerName: String,
        customerImageUrl: String,
        comment: String,
        date: Date,
        rate: RatingBar
    ) {
        this.customerName = customerName
        this.customerImageUrl = customerImageUrl
        this.comment = comment
        this.date = date
        this.rate = rate
    }

    constructor()
}