package com.shoplex.shoplex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoplex.shoplex.model.enumurations.DeliveryMethod
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.model.pojo.Checkout

class CheckoutVM: ViewModel {
    var deliveryMethod: MutableLiveData<DeliveryMethod> = MutableLiveData()
    var paymentMethod: MutableLiveData<PaymentMethod> = MutableLiveData()

    constructor()
}