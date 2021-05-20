package com.shoplex.shoplex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoplex.shoplex.model.firebase.StoresDBModel
import com.shoplex.shoplex.model.interfaces.INotifyMVP
import com.shoplex.shoplex.model.pojo.Store

class DetailsVM :ViewModel,INotifyMVP{
    var store: MutableLiveData<Store> = MutableLiveData()
    private var storesDBModel = StoresDBModel(this)
    constructor()

    fun getStoreData(storeID:String){
        storesDBModel.getStoreById(storeID)

    }

    override fun onStoreInfoReady(stores: ArrayList<Store>) {
        if (stores.count()>0) {
            this.store.value = stores[0]
        }
    }
}