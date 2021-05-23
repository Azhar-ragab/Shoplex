package com.shoplex.shoplex.model.pojo

class StoreLocationInfo {
    var storeName: String = ""
    var distance: Float = 0F
    var carTime: Int = 0
    var walkingTime: Int = 0

    constructor(storeName: String, distance: Float, carTime: Int, walkingTime: Int) {
        this.storeName = storeName
        this.distance = distance
        this.carTime = carTime
        this.walkingTime = walkingTime
    }
}