package com.shoplex.shoplex.viewmodel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.R
import com.shoplex.shoplex.model.enumurations.AuthType
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.firebase.AuthDBModel
import com.shoplex.shoplex.model.interfaces.UserActionListener
import com.shoplex.shoplex.model.pojo.User
import com.shoplex.shoplex.view.activities.HomeActivity
import com.shoplex.shoplex.view.activities.LoginActivity
import com.shoplex.shoplex.view.activities.SignupActivity

class AuthVM(val context: Context): ViewModel(), UserActionListener {
    var user: MutableLiveData<User> = MutableLiveData()
    var email: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()

    private var userDBModel: AuthDBModel

    init {
        this.user.value = User()
        this.email.value = ""
        this.password.value = ""
        this.userDBModel = AuthDBModel(this, context)
    }

    fun login(authType: AuthType, accessToken: AccessToken? = null) {
        when (authType) {
            AuthType.Email -> userDBModel.loginWithEmail(email.value!!, password.value!!)
            AuthType.Facebook -> userDBModel.loginWithFacebook(accessToken!!)
            AuthType.Google -> {
            }
        }
    }

    fun createAccount() {
        //Toast.makeText(context, password.value.toString(), Toast.LENGTH_SHORT).show()
        userDBModel.createEmailAccount(user.value!!, password.value!!)
    }

    fun addUser() {
        userDBModel.addNewUser(user.value!!)
    }

    override fun onAddNewUser(user: User?) {
        super.onAddNewUser(user)
        if(user != null){
            UserInfo.saveUserInfo(context)
            (context as SignupActivity).finish()
        }
    }

    override fun onLoginSuccess(user: User) {
        super.onLoginSuccess(user)
        // context.startActivity(Intent(context, HomeActivity::class.java))
        Toast.makeText(context, context.getString(R.string.login_success), Toast.LENGTH_SHORT).show()
        (context as LoginActivity).finish()
    }

    override fun onLoginFailed() {
        super.onLoginFailed()
        Toast.makeText(context, "Login Failed!", Toast.LENGTH_SHORT).show()
        UserInfo.clear()
    }
}