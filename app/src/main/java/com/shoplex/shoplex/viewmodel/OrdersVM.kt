package com.shoplex.shoplex.viewmodel

import androidx.lifecycle.ViewModel
import com.shoplex.shoplex.model.firebase.OrdersDBModel
import com.shoplex.shoplex.model.interfaces.INotifyMVP
import com.shoplex.shoplex.model.pojo.Order

class OrdersVM: ViewModel , INotifyMVP {

    private val ordersDBModel = OrdersDBModel(this)

    constructor(){

    }

    fun addOrder(order: Order){
        ordersDBModel.addOrder(order)
    }

    override fun onOrderSuccess() {

    }

    override fun onOrderFailed() {

    }
}