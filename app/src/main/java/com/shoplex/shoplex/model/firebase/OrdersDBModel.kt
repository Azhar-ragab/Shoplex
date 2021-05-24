package com.shoplex.shoplex.model.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
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

    fun getCurrentOrders() {

        FirebaseReferences.ordersRef.whereEqualTo("userID", UserInfo.userID).whereEqualTo("orderStatus","Current")
            .addSnapshotListener { values, _ ->
                var orders = arrayListOf<Order>()
                for (document: DocumentSnapshot in values?.documents!!) {
                    var order: Order? = document.toObject<Order>()
                    if (order != null) {
                        orders.add(order)
                        FirebaseReferences.productsRef.document(order.productID).get().addOnSuccessListener {
                            order.product=it.toObject<Product>()
                            if (document==values.last()){
                                this.notifier?.onCurrentOrdersReady(orders)
                            }
                        }
                    }
                }

            }
    }
}