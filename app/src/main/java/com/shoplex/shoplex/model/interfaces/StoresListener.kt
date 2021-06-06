package com.shoplex.shoplex.model.interfaces

import com.shoplex.shoplex.model.pojo.Store
import com.shoplex.shoplex.model.pojo.StoreLocationInfo

interface StoresListener {
    fun onStoreInfoReady(stores: ArrayList<Store>){}
    fun onStoresReady(storesLocationInfo: ArrayList<StoreLocationInfo>){}
}