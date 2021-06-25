package com.shoplex.shoplex.model.firebase

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.model.enumurations.Category
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.interfaces.ProductsListener
import com.shoplex.shoplex.model.pojo.*

class ProductsDBModel(private val notifier: ProductsListener?) {

    fun getAllProducts(category: String, filter: Filter, sort: Sort?) {
        var query: Query = FirebaseReferences.productsRef
            .whereEqualTo("category", category)

        if(filter.lowPrice != null && filter.highPrice != null)
            query = query.whereGreaterThanOrEqualTo("newPrice", filter.lowPrice)
                .whereLessThanOrEqualTo("newPrice", filter.highPrice)

        if (filter.shops != null)
            query = query.whereIn("storeID", filter.shops)

        if (sort != null) {

            if (sort.price == false)
                query = query.orderBy("newPrice", Query.Direction.ASCENDING)
            else if (sort.price == true)
                query = query.orderBy("newPrice", Query.Direction.DESCENDING)

            if(sort.rate)
                query = query.orderBy("rate", Query.Direction.DESCENDING)

            if(sort.discount)
                query = query.orderBy("discount", Query.Direction.DESCENDING)
        }

        query.addSnapshotListener { values, error ->
            if(error != null){
                Log.i("FIREBASEINDEXES", error.toString())
            }

            val products = arrayListOf<Product>()
            for (document: DocumentSnapshot in values?.documents!!) {
                val product: Product? = document.toObject<Product>()
                if (product != null) {
                    var pass = true

                    if(filter.subCategory != null && !filter.subCategory.contains(product.subCategory))
                        pass = false

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
                val products = arrayListOf<Product>()
                for (document: DocumentSnapshot in values?.documents!!) {
                    val product: Product? = document.toObject<Product>()
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
                val products = arrayListOf<Product>()
                for (document: DocumentSnapshot in values?.documents!!) {
                    val product: Product? = document.toObject<Product>()
                    if (product != null) {
                        products.add(product)
                    }
                }
                this.notifier?.onAllAdvertisementsReady(products)
            }
    }

    fun getReviewByProductId(productId: String) {
        FirebaseReferences.productsRef.document(productId).collection("Reviews")
            .addSnapshotListener  { values,_ ->
                val reviews = arrayListOf<Review>()
                for (document in values?.documents!!) {
                    val review: Review? = document.toObject<Review>()
                    if (review != null) {
                        reviews.add(review)
                    }
                }
                this.notifier?.onAllReviewsReady(reviews)
            }
    }

    fun getReviewsStatistics(productId: String){
        FirebaseReferences.productsRef.document(productId).collection("Statistics").document("Reviews").get().addOnSuccessListener {
            if(it.exists()) {
                val statistic: ReviewStatistics = it.toObject()!!
                this.notifier?.onReviewStatisticsReady(statistic)
            }

        }
    }

}