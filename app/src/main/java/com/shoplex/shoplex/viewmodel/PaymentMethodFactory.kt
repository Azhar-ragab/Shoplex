package com.shoplex.shoplex.viewmodel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shoplex.shoplex.model.interfaces.PaymentListener

class PaymentMethodFactory(val context: Context, val fragment: Fragment, val listener: PaymentListener): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = PaymentMethodVM(context, fragment, listener) as T
}