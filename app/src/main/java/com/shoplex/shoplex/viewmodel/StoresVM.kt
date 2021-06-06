package com.shoplex.shoplex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoplex.shoplex.model.enumurations.Category
import com.shoplex.shoplex.model.firebase.StoresDBModel
import com.shoplex.shoplex.model.interfaces.StoresListener
import com.shoplex.shoplex.model.pojo.StoreLocationInfo

class StoresVM: ViewModel, StoresListener {
    private val storesDBModel = StoresDBModel(this)
    var subCatCheckList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    var storesList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    var selectedItem: MutableLiveData<String> = MutableLiveData()
    var storesLocationInfo: MutableLiveData<ArrayList<StoreLocationInfo>> = MutableLiveData()

    constructor(){
        subCatCheckList.value = arrayListOf()
        storesList.value = arrayListOf()
        selectedItem.value = Category.Fashion.name
    }

    fun getStoresInfo(){
        storesDBModel.getStores(selectedItem.value!!)
    }

    override fun onStoresReady(storesLocationInfo: ArrayList<StoreLocationInfo>) {
        this.storesLocationInfo.value = storesLocationInfo
    }
}