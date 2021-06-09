package com.shoplex.shoplex.model.firebase

import android.content.Context
import android.util.Log
import android.util.Range
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.Review
import com.shoplex.shoplex.model.enumurations.Category
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.interfaces.INotifyMVP
import com.shoplex.shoplex.model.pojo.Filter
import com.shoplex.shoplex.model.pojo.Sort
import com.shoplex.shoplex.model.pojo.StoreLocationInfo
import java.util.*

class ProductsDBModel(val notifier: INotifyMVP?) {

    /*
    fun getAllProducts(category: Category) {
        FirebaseReferences.productsRef.whereEqualTo("category", category.name)
            .addSnapshotListener { values, _ ->
                var products = arrayListOf<Product>()
                for (document: DocumentSnapshot in values?.documents!!) {
                    var product: Product? = document.toObject<Product>()
                    if (product != null) {
                        products.add(product)
                    }
                }
                this.notifier?.onAllProductsReady(products)
            }
    }
    */


    fun getAllProducts(category: Category, filter: Filter, sort: Sort?) {
        var query: Query = FirebaseReferences.productsRef
            .whereEqualTo("category", category.name.replace("_"," "))

        if(filter.lowPrice != null && filter.highPrice != null)
            query = query.whereGreaterThanOrEqualTo("price", filter.lowPrice)
                .whereLessThanOrEqualTo("price", filter.highPrice)
                .orderBy("price", Query.Direction.ASCENDING)

        if(filter.subCategory != null)
            query = query.whereIn("subCategory", filter.subCategory)

        if (filter.shops != null)
            query = query.whereIn("storeID", filter.shops)

        if (sort != null) {

            if (sort.price == false)
                query = query.orderBy("price", Query.Direction.ASCENDING)
            else if (sort.price == true)
                query = query.orderBy("price", Query.Direction.DESCENDING)

            if(sort.rate)
                query = query.orderBy("rate", Query.Direction.DESCENDING)

            if(sort.discount)
                query = query.orderBy("discount", Query.Direction.DESCENDING)
        }

        query.addSnapshotListener { values, _ ->
            if(values == null)
                return@addSnapshotListener
            var products = arrayListOf<Product>()
            for (document: DocumentSnapshot in values?.documents!!) {
                var product: Product? = document.toObject<Product>()
                if (product != null) {
                    var pass = true
                    /*
                    if(filter.subCategory!= null && product.subCategory != filter.subCategory)
                        pass = false
                    */

                    if(filter.rate != null && (product.rate == null || product.rate!! < filter.rate))
                        pass = false

                    if(filter.discount != null && product.discount < filter.discount)
                        pass = false

                    if(pass)
                        products.add(product)
                }
            }
            this.notifier?.onAllProductsReady(products)
        }
    }

    fun getProductById(productId: String) {

        FirebaseReferences.productsRef.whereEqualTo("productID", productId)
            .addSnapshotListener { values, _ ->
                var products = arrayListOf<Product>()
                for (document: DocumentSnapshot in values?.documents!!) {
                    var product: Product? = document.toObject<Product>()
                    if (product != null) {
                        products.add(product)
                    }
                }
                this.notifier?.onAllProductsReady(products)
            }
    }

    fun getAllPremiums() {

        FirebaseReferences.productsRef.whereGreaterThan("premium.premiumDays", 0)
            .addSnapshotListener { values, _ ->
                var products = arrayListOf<Product>()
                for (document: DocumentSnapshot in values?.documents!!) {
                    var product: Product? = document.toObject<Product>()
                    if (product != null) {
                        products.add(product)
                    }
                }
                this.notifier?.onAllAdvertismentsReady(products)
            }
    }
    fun getReviewByProductId(productId: String) {

        FirebaseReferences.productsRef.document(productId).collection("Reviews")
            .addSnapshotListener  { values,_ ->
                var reviews = arrayListOf<Review>()
                for (document in values?.documents!!) {
                    var review: Review? = document.toObject<Review>()
                    if (review != null) {
                        reviews.add(review)
                    }
                }
                this.notifier?.onAllReviwsReady(reviews)
            }
    }

}