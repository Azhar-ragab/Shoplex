package com.shoplex.shoplex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoplex.shoplex.model.pojo.Checkout

class CheckoutVM: ViewModel {

    var checkout: MutableLiveData<Checkout> = MutableLiveData()

    constructor(){
        checkout.value = Checkout()
    }
}