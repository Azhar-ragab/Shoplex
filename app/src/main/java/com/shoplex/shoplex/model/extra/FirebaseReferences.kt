package com.shoplex.shoplex.model.extra

import android.annotation.SuppressLint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

object FirebaseReferences {
    // -----------------------> Databases <----------------------- //
    @SuppressLint("StaticFieldLeak")
    private val database = Firebase.firestore
    private val imagesDatabase = FirebaseStorage.getInstance().reference

    // -----------------------> Firestore <----------------------- //
    // Products
    val productsRef = database.collection("Products")
    val storesRef = database.collection("Sellers")
    val ordersRef = database.collection("Orders")

    // -----------------------> Storage <----------------------- //
    // Products
    val imagesProductsRef = imagesDatabase.child("Products")
}