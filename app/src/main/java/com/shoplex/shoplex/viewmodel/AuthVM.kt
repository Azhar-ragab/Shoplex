package com.shoplex.shoplex.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoplex.shoplex.R
import com.shoplex.shoplex.model.enumurations.AuthType
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.firebase.AuthDBModel
import com.shoplex.shoplex.model.interfaces.UserActionListener
import com.shoplex.shoplex.model.pojo.User
import com.shoplex.shoplex.view.activities.LoginActivity

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

    fun login(authType: AuthType, accessToken: String? = null) {
        when (authType) {
            AuthType.Email -> userDBModel.loginWithEmail(email.value!!, password.value!!)
            AuthType.Facebook -> userDBModel.loginWithFacebook(accessToken!!)
            AuthType.Google -> userDBModel.loginWithGoogle(accessToken!!)
        }
    }

    fun createAccount() {
        //Toast.makeText(context, password.value.toString(), Toast.LENGTH_SHORT).show()
        userDBModel.createEmailAccount(user.value!!, password.value!!)
    }

    /*
    fun addUser() {
        userDBModel.addNewUser(user.value!!)
    }
    */

    override fun onAddNewUser(context: Context, user: User?) {
        super.onAddNewUser(context, user)
        if(user != null){
            UserInfo.saveUserInfo(context)
            (context as AppCompatActivity).finish()
        }
    }

    override fun onLoginSuccess(context: Context, user: User) {
        super.onLoginSuccess(context, user)
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