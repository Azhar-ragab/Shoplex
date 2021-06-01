package com.shoplex.shoplex.model.interfaces

import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.User

interface UserActionListener {
    fun showIndicator(){}
    fun hideIndicator(){}

    fun onAddNewUser(user: User?){
        if(user != null) {
            onUserInfoReady(user = user)
        }
    }

    private fun onUserInfoReady(user: User){
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
    }

    fun onLoginSuccess(user: User){
        onUserInfoReady(user)
    }

    fun onLoginFailed(){
        UserInfo.clear()
    }
}