package com.shoplex.shoplex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.model.firebase.OrdersDBModel
import com.shoplex.shoplex.model.interfaces.INotifyMVP
import com.shoplex.shoplex.model.pojo.Order

class OrdersVM: ViewModel , INotifyMVP {

    private val ordersDBModel = OrdersDBModel(this)
    var orders: MutableLiveData<ArrayList<Order>> = MutableLiveData()
    var lastOrders:MutableLiveData<ArrayList<Order>> = MutableLiveData()
    constructor(){

    }

    fun addOrder(order: Order){
        ordersDBModel.addOrder(order)
    }

    override fun onOrderSuccess() {

    }

    override fun onOrderFailed() {

    }

    override fun onCurrentOrdersReady(orders: ArrayList<Order>) {
       this.orders.value=orders

    }
    override fun onLastOrdersReady(lastOrders: ArrayList<Order>) {
        this.lastOrders.value=lastOrders
    }
    fun getCurrentOrders(){
        ordersDBModel.getCurrentOrders()
    }
    fun getLastOrders(){
        ordersDBModel.getLastOrders()
    }
}