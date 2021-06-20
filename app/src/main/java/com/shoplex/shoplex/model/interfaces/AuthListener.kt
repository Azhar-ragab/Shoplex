package com.shoplex.shoplex.model.interfaces

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Cart
import com.shoplex.shoplex.model.pojo.Favorite
import com.shoplex.shoplex.model.pojo.User
import com.shoplex.shoplex.room.data.ShopLexDataBase
import com.shoplex.shoplex.room.repository.FavoriteCartRepo
import kotlinx.coroutines.launch

interface AuthListener {
    fun showIndicator(){}
    fun hideIndicator(){}

    fun onAddNewUser(context: Context, user: User?){
        if(user != null) {
            Toast.makeText(context, "Success to create your account!", Toast.LENGTH_SHORT).show()
            FirebaseReferences.usersRef.document(user.userID)
                .collection("Lists")
                .document("Favorite").set(Favorite())
            FirebaseReferences.usersRef.document(user.userID)
                .collection("Lists")
                .document("Cart").set(Cart())
            onUserInfoReady(context, user = user)
        }else{
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onUserInfoReady(context: Context, user: User){
        UserInfo.userID = user.userID
        UserInfo.image = user.image
        UserInfo.name = user.name
        UserInfo.image = user.image
        UserInfo.email = user.email
        UserInfo.location = user.location
        UserInfo.address = user.address
        UserInfo.phone = user.phone
        (context as AppCompatActivity).lifecycleScope.launch {
            FavoriteCartRepo(ShopLexDataBase.getDatabase(context).shopLexDao()).syncProducts(context)
        }
        UserInfo.updateTokenID()
        UserInfo.saveUserInfo(context)
        UserInfo.saveToRecentVisits()
        UserInfo.saveNotification(context, true)
    }

    fun onLoginSuccess(context: Context, user: User){
        onUserInfoReady(context, user)
    }

    fun onLoginFailed(){
        UserInfo.clear()
    }
}