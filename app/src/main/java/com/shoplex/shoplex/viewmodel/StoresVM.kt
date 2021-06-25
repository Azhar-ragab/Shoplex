package com.shoplex.shoplex.viewmodel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoplex.shoplex.R
import com.shoplex.shoplex.model.enumurations.Category
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
    var arrSubCats: Array<String> = arrayOf()

    init {
        subCatCheckList.value = arrayListOf()
        storesList.value = arrayListOf()
        val selectedIndex =  context.resources.getStringArray(R.array.categories).indexOf(category)
        selectedItem.value = Category.values()[selectedIndex].name.replace("_", " ")
        getStoresByCategory()
    }

    private fun getStoresByCategory() {
        storesDBModel.getStores(selectedItem.value!!)
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
