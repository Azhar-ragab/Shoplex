package com.shoplex.shoplex.model.pojo

import androidx.room.Entity

@Entity(tableName = "StoresLocation", primaryKeys = ["storeID","location"])
data class StoreLocationInfo(val storeID : String = "", val location: Location = Location(), val storeName: String? = null, val distance: String? = null, val duration: String? = null)
/*{
    var storeID : String = ""
    var storeName: String = ""
    var distance: Float = 0F
    var carTime: Int = 0
    var walkingTime: Int = 0

    constructor(storeID : String, storeName: String){
        this.storeID = storeID
        this.storeName = storeName
    }

    constructor {
        this.storeID = storeID
        this.storeName = storeName
        this.distance = distance
        this.carTime = carTime
        this.walkingTime = walkingTime
    }
}
 */