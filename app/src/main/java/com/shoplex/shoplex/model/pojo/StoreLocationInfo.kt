package com.shoplex.shoplex.model.pojo

class StoreLocationInfo {
    var storeID : String = ""
    var storeName: String = ""
    var distance: Float = 0F
    var carTime: Int = 0
    var walkingTime: Int = 0

    constructor(storeID : String, storeName: String){
        this.storeID = storeID
        this.storeName = storeName
    }

    constructor(storeID : String, storeName: String, distance: Float, carTime: Int, walkingTime: Int) {
        this.storeID = storeID
        this.storeName = storeName
        this.distance = distance
        this.carTime = carTime
        this.walkingTime = walkingTime
    }
}