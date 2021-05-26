package com.shoplex.shoplex.model.firebase

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.Review
import com.shoplex.shoplex.model.enumurations.Category
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.interfaces.INotifyMVP
import java.util.*

class ProductsDBModel(val notifier: INotifyMVP?) {

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

        FirebaseReferences.productsRef.whereGreaterThan("premiumDays", 0)
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