package com.shoplex.shoplex.room.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.shoplex.shoplex.model.pojo.StoreLocationInfo
import com.shoplex.shoplex.room.data.ShoplexDataBase
import com.shoplex.shoplex.room.repository.StoreLocInfoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StoreLocInfoVM(application: Application) : AndroidViewModel(application) {

    val readStoreLocInfo: LiveData<List<StoreLocationInfo>>
    private val storeLocInfoRepo: StoreLocInfoRepo

    init {
        val storeLocInfoDao = ShoplexDataBase.getDatabase(application).shoplexDao()
        storeLocInfoRepo = StoreLocInfoRepo(storeLocInfoDao)
        readStoreLocInfo = storeLocInfoRepo.readStoreLocInfo
    }

    fun addStoreLocInfo(locInfo: StoreLocationInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            storeLocInfoRepo.addStoreLocInfo(locInfo)
        }
    }

    fun deleteStoreLocInfo(locInfo: StoreLocationInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            storeLocInfoRepo.deleteStoreLocInfo(locInfo)
        }
    }

    fun updateStoreLocInfo(locInfo: StoreLocationInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            storeLocInfoRepo.updateStoreLocInfo(locInfo)
        }
    }
}