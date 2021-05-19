package com.shoplex.shoplex.model.pojo

class Products_Home {

    var productName: String = ""
    var oldPrice: Float = 0.0F
    var newPrice: Float = 0.0F
    var rate: Double? = null
    var soldItem: String = ""
    var storeName: String = ""
    var space : String =""
    var productImageURL:String = ""
    var productsID: Int =0


    constructor(
        productName: String,
        oldPrice: Float,
        newPrice: Float,
        rate: Double?,
        soldItem: String,
        storeName: String,
        space: String,
        productImageURL: String,
        productsID: Int
    ) {
        this.productName = productName
        this.oldPrice = oldPrice
        this.newPrice = newPrice
        this.rate = rate
        this.soldItem = soldItem
        this.storeName = storeName
        this.space = space
        this.productImageURL = productImageURL
        this.productsID = productsID
    }
    constructor()
}