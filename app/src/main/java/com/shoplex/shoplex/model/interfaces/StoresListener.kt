package com.shoplex.shoplex.model.interfaces

import com.shoplex.shoplex.model.pojo.Store

interface StoresListener {
    fun onStoreInfoReady(stores: ArrayList<Store>){}
    fun onStoresIDsReady(storesIDs: ArrayList<String>){}
}