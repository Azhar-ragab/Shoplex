package com.shoplex.shoplex.room.repository

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Product
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.ProductFavourite
import com.shoplex.shoplex.model.pojo.StoreLocationInfo
import com.shoplex.shoplex.room.data.ShopLexDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteCartRepo(private val shopLexDao: ShopLexDao) {

    var productID = MutableStateFlow("")
    var storeIfo = MutableStateFlow(StoreLocationInfo())

    // Favorite
    val favoriteProducts: LiveData<List<ProductFavourite>> = shopLexDao.readFavourite()

    val searchFavouriteByID = productID.flatMapLatest {
        shopLexDao.searchFav(it)
    }.asLiveData()

    val storeLocationInfo = storeIfo.flatMapLatest {
        shopLexDao.getLocation(it.storeID, it.location)
    }.asLiveData()

    suspend fun addFavourite(favourite: ProductFavourite) {
        shopLexDao.addFavourite(favourite)
    }

    suspend fun deleteFavourite(productID: String) {
        shopLexDao.deleteFavourite(productID)
    }

    // Cart
    val cartProducts: LiveData<List<ProductCart>> = shopLexDao.readCart()

    val searchCartByID = productID.flatMapLatest {
        shopLexDao.searchCart(it)
    }.asLiveData()

    suspend fun addCart(cart: ProductCart) {
        shopLexDao.addCart(cart)
    }

    suspend fun deleteCart(productID: String) {
        shopLexDao.deleteCart(productID)
    }

    suspend fun updateCart(productID: String, quantity: Int) {
        shopLexDao.updateCart(productID, quantity)
    }

    suspend fun addNewLocation(location: StoreLocationInfo) {
        shopLexDao.addLocation(location)
    }

    suspend fun syncProducts(context: Context) {
        var favProductsList: Array<String> = arrayOf()
        var cartProductsList: Array<String> = arrayOf()

        FirebaseReferences.usersRef.document(UserInfo.userID!!).collection("Lists")
            .get().addOnSuccessListener {

                GlobalScope.launch {
                    withContext(Dispatchers.Main){
                        favoriteProducts.observe(context as AppCompatActivity, { favProducts ->
                            favProductsList = favProducts.groupBy { productFav ->
                                productFav.productID
                            }.map { mapEntry ->
                                mapEntry.key
                            }.toTypedArray()

                            FirebaseReferences.usersRef.document(UserInfo.userID!!)
                                .collection("Lists")
                                .document("Favorite")
                                .update("favoriteList", FieldValue.arrayUnion(*favProductsList))
                        })
                    }
                }

                GlobalScope.launch {
                    withContext(Dispatchers.Main){
                        cartProducts.observe(context as AppCompatActivity, { cartProducts ->
                            cartProductsList = cartProducts.groupBy { productFav ->
                                productFav.productID
                            }.map { mapEntry ->
                                mapEntry.key
                            }.toTypedArray()

                            FirebaseReferences.usersRef.document(UserInfo.userID!!)
                                .collection("Lists")
                                .document("Cart")
                                .update("cartList", FieldValue.arrayUnion(*cartProductsList))
                        })
                    }
                }


                for (document in it.documents) {
                    if (document.reference.id == "Cart") {
                        val cartList = document.get("cartList") as ArrayList<String>

                        for (cartID in cartList) {
                          //  if (!cartProductsList.contains(cartID)) {
                                FirebaseReferences.productsRef.document(cartID).get()
                                    .addOnSuccessListener { result ->
                                        if (result.exists()) {
                                            val product = result.toObject<Product>()
                                            GlobalScope.launch {
                                                addCart(ProductCart(product = product!!))
                                            }
                                        }
                                    }
                          //  }
                        }
                    }

                    if (document.reference.id == "Favorite") {
                        val favoriteList = document.get("favoriteList") as ArrayList<String>

                        for (favID in favoriteList) {
                          //  if (!favProductsList.contains(favID)) {
                                FirebaseReferences.productsRef.document(favID).get()
                                    .addOnSuccessListener { result ->
                                        if (result.exists()) {
                                            val product = result.toObject<Product>()
                                            GlobalScope.launch {
                                                addFavourite(ProductFavourite(product!!))
                                            }
                                        }
                                    }
                    //        }
                        }
                    }
                }
            }
    }
}