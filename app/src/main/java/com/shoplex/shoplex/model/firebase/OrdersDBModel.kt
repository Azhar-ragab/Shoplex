package com.shoplex.shoplex.model.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.interfaces.INotifyMVP
import com.shoplex.shoplex.model.pojo.Order

class OrdersDBModel(val notifier: INotifyMVP) {

    fun addOrder(order: Order) {
        FirebaseReferences.ordersRef.document(order.orderID).set(order).addOnSuccessListener {
            notifier.onOrderSuccess()
        }.addOnFailureListener {
            notifier.onOrderFailed()
        }
    }
}