package com.shoplex.shoplex.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoplex.shoplex.model.firebase.UserDBModel
import com.shoplex.shoplex.model.interfaces.UserActionListener
import com.shoplex.shoplex.model.pojo.User

class UserVM(val context: Context): ViewModel(), UserActionListener{
    var user: MutableLiveData<User> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()

    private var userDBModel: UserDBModel

   init {
       this.user.value = User()
       this.password.value = ""
       this.userDBModel = UserDBModel(this)
    }

    fun createAccount(){
        Toast.makeText(context, password.value.toString(), Toast.LENGTH_SHORT).show()
        //userDBModel.createAccount(user.value!!, password.value!!)
    }

    fun addUser() {
        userDBModel.addNewUser(user.value!!)
    }

    override fun onAddNewUser(isAdded: Boolean) {
        if(isAdded){

        }else{

        }
    }
}