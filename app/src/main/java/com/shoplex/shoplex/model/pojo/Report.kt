package com.shoplex.shoplex

import java.util.*

class Report {

    var customerName : String = ""
    var reportComment : String = ""
    lateinit var date : Date


    constructor()
    constructor(customerName: String, reportComment: String, date: Date) {
        this.customerName = customerName
        this.reportComment = reportComment
        this.date = date
    }

}