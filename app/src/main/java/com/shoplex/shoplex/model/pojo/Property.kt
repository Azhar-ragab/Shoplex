package com.shoplex.shoplex

class Property {

    var propertyID : Int = 0
    var name : String = ""
    var values: ArrayList<String> = arrayListOf()
    constructor()
    constructor(name: String, values: ArrayList<String>) {
        this.name = name
        this.values = values
    }
}