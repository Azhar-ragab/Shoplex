package com.shoplex.shoplex.room.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FavouriteFactoryModel(val context: Context, val productID: String): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = FavouriteViewModel(context,productID) as T
}