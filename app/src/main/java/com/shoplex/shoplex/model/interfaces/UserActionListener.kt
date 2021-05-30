package com.shoplex.shoplex.model.interfaces

import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.User

interface UserActionListener {
    fun showIndicator(){}
    fun hideIndicator(){}
    fun onAddNewUser(isAdded: Boolean){}
    fun onUserInfoReady(user: User){
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
    }
    fun onNewFacebookAccountCreated(){}
    fun onUserInfoFailed(){
        UserInfo.clear()
    }
}