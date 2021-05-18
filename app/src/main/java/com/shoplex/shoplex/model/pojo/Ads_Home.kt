package com.shoplex.shoplex.model.pojo

class Ads_Home {
    var productName: String = ""
    var productImageURL:String = ""
    var prouductID:Int =0
var productOffer: String =""
    constructor(productName: String, productImageURL: String, productOffer: String) {
        this.productName = productName
        this.productImageURL = productImageURL
        this.productOffer = productOffer
    }

    constructor()

}