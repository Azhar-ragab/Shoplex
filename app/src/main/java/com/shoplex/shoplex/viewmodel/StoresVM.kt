package com.shoplex.shoplex.viewmodel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoplex.shoplex.model.firebase.StoresDBModel
import com.shoplex.shoplex.model.interfaces.StoresListener
import com.shoplex.shoplex.model.pojo.StoreLocationInfo
import com.shoplex.shoplex.room.data.ShopLexDataBase
import com.shoplex.shoplex.room.repository.FavoriteCartRepo

class StoresVM(val context: Context, val category: String) : ViewModel(), StoresListener {
    private val storesDBModel = StoresDBModel(this)
    var subCatCheckList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    var storesList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    var selectedItem: MutableLiveData<String> = MutableLiveData()
    var storesLocationInfo: MutableLiveData<ArrayList<StoreLocationInfo>> = MutableLiveData()

    init {
        subCatCheckList.value = arrayListOf()
        storesList.value = arrayListOf()
        selectedItem.value = category
        getStoresByCategory()
    }

    private fun getStoresByCategory() {
        storesDBModel.getStores(category)
    }

    override fun onStoresIDsReady(storesIDs: ArrayList<String>) {

        if (!storesIDs.isNullOrEmpty() && storesLocationInfo.value.isNullOrEmpty()) {
            val repo = FavoriteCartRepo(ShopLexDataBase.getDatabase(context).shopLexDao())
            repo.storesIDs.value = storesIDs
            repo.storesLocationInfo.observe(context as AppCompatActivity, {
                if (it.size == 1)
                    storesLocationInfo.value = arrayListOf(it.first())
                else if (it.size > 1)
                    storesLocationInfo.value = it.toList() as ArrayList<StoreLocationInfo>
            })
        }
    }
}
