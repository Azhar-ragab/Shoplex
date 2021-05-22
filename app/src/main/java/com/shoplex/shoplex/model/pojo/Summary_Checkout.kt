package com.shoplex.shoplex.model.pojo

class Summary_Checkout {
    var ProductId :Int =0
    var productName :String =""
    var Quantity :String =""
    var Price : Float = 0.0F
    var productImageURL:String = ""

    constructor(productName: String, Quantity: String, Price: Float, productImageURL: String) {
        this.productName = productName
        this.Quantity = Quantity
        this.Price = Price
        this.productImageURL = productImageURL
    }

    constructor()


}