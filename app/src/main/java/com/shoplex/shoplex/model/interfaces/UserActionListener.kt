package com.shoplex.shoplex.model.interfaces

import android.content.Context
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.User

interface UserActionListener {
    fun showIndicator(){}
    fun hideIndicator(){}

    fun onAddNewUser(context: Context, user: User?){
        if(user != null) {
            onUserInfoReady(context, user = user)
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
        UserInfo.favouriteList = user.favouriteList
        UserInfo.cartList = user.cartList
        UserInfo.updateTokenID()
        UserInfo.saveUserInfo(context)
    }

    fun onLoginSuccess(context: Context, user: User){
        onUserInfoReady(context, user)
    }

    fun onLoginFailed(){
        UserInfo.clear()
    }
}