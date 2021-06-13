package com.shoplex.shoplex.model.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.model.pojo.Product
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.interfaces.StoresListener
import com.shoplex.shoplex.model.pojo.Store
import com.shoplex.shoplex.model.pojo.StoreLocationInfo

class StoresDBModel(val notifier:StoresListener) {

    fun getStoreById(storeId:String){
        FirebaseReferences.storesRef.whereEqualTo("storeID",storeId).get().addOnCompleteListener {values->
            var stores = arrayListOf<Store>()
            for (document: DocumentSnapshot in values.result!!) {
                var store: Store? = document.toObject<Store>()
                if (store != null) {
                    stores.add(store)
                }
            }
            this.notifier.onStoreInfoReady(stores)
        }
    }

    fun getStores(category: String){
        FirebaseReferences.productsRef.whereEqualTo("category", category)
            .addSnapshotListener { values, _ ->
                var products = arrayListOf<Product>()
                var storesInfo = arrayListOf<StoreLocationInfo>()
                for (document: DocumentSnapshot in values?.documents!!) {
                    var product: Product? = document.toObject<Product>()
                    if (product != null) {
                        products.add(product)
                    }
                }

                products.groupBy { it.storeID }.forEach {
                    val item = it.value[0]
                    storesInfo.add(StoreLocationInfo(item.storeID, item.storeLocation))
                }

                this.notifier.onStoresReady(storesInfo)
            }
    }
}