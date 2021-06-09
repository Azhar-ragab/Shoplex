package com.shoplex.shoplex.room.repository

import androidx.lifecycle.LiveData
import com.shoplex.shoplex.model.pojo.StoreLocationInfo
import com.shoplex.shoplex.room.data.ShoplexDao


class StoreLocInfoRepo(private val shoplexDao: ShoplexDao) {

    val readStoreLocInfo: LiveData<List<StoreLocationInfo>> = shoplexDao.readStoreLocInfo()

    suspend fun addStoreLocInfo(locInfo: StoreLocationInfo) {
        shoplexDao.addStoreLocInfo(locInfo)
    }

    suspend fun deleteStoreLocInfo(locInfo: StoreLocationInfo) {
        shoplexDao.deleteStoreLocInfo(locInfo)
    }

    suspend fun updateStoreLocInfo(locInfo: StoreLocationInfo) {
        shoplexDao.updateStoreLocInfo(locInfo)
    }
}