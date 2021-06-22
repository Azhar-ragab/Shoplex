package com.shoplex.shoplex.model.pojo

import androidx.room.Entity

@Entity(tableName = "StoresLocation", primaryKeys = ["storeID", "location"])
data class StoreLocationInfo(
    val storeID: String = "",
    val location: Location = Location(),
    val storeName: String? = null,
    val distance: String? = null,
    val duration: String? = null
)
