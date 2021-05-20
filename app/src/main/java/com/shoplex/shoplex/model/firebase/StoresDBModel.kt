package com.shoplex.shoplex.model.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.interfaces.INotifyMVP
import com.shoplex.shoplex.model.pojo.Store

class StoresDBModel(val notifier:INotifyMVP) {

    fun getStoreById(storeId:String){
        FirebaseReferences.storesRef.whereEqualTo("storeID",storeId).get().addOnCompleteListener {values->
            var stores = arrayListOf<Store>()
            for (document: DocumentSnapshot in values.result!!) {
                var store: Store? = document.toObject<Store>()
                if (store != null) {
                    stores.add(store)
                }
            }
            this.notifier?.onStoreInfoReady(stores)
        }

    }
}