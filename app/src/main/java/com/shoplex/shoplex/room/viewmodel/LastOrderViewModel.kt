package com.shoplex.shoplex.room.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.shoplex.shoplex.model.pojo.LastOrder
import com.shoplex.shoplex.room.data.ShoplexDataBase
import com.shoplex.shoplex.room.repository.LastOrderRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//class LastOrderViewModel(application: Application) :AndroidViewModel(application) {
//
//    private val readAllLastOrder : LiveData<List<LastOrder>>
//    private val lastOrderRepo : LastOrderRepo
//
//    init {
//        val lastOrderDoa = ShoplexDataBase.getDatabase(application).lastOrderDoa()
//        lastOrderRepo = LastOrderRepo(lastOrderDoa)
//        readAllLastOrder = lastOrderRepo.readAllLastOrder
//    }
//
//    fun addLastOrder(lastOrder: List<LastOrder>){
//        viewModelScope.launch(Dispatchers.IO) {
//            lastOrderRepo.addLastOrder(lastOrder)
//        }
//    }
//}