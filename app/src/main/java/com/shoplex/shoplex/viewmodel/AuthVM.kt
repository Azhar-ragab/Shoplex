package com.shoplex.shoplex.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.R
import com.shoplex.shoplex.model.enumurations.AuthType
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.firebase.AuthDBModel
import com.shoplex.shoplex.model.interfaces.AuthListener
import com.shoplex.shoplex.model.pojo.User
import com.shoplex.shoplex.room.data.ShopLexDataBase
import com.shoplex.shoplex.view.activities.auth.AuthActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AuthVM(val context: Context) : ViewModel(), AuthListener {
    var user: MutableLiveData<User> = MutableLiveData()
    var email: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()

    var isLoginBtnPressed: MutableLiveData<Boolean> = MutableLiveData()
    var isSignupBtnPressed: MutableLiveData<Boolean> = MutableLiveData()

    var isLoginValid: MutableLiveData<Boolean> = MutableLiveData()
    var isSignupValid: MutableLiveData<Boolean> = MutableLiveData()

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
        Firebase.auth.fetchSignInMethodsForEmail(user.value!!.email).addOnCompleteListener {
            if (it.isSuccessful && it.result?.signInMethods?.size == 0) {
                userDBModel.createEmailAccount(user.value!!, password.value!!)
            } else {
                onUserExists()
            }
        }
    }

    override fun onAddNewUser(context: Context, user: User?) {
        super.onAddNewUser(context, user)
        if (user != null) {
            UserInfo.saveUserInfo(context)
            (context as AppCompatActivity).finish()
        }
    }

    override fun onLoginSuccess(context: Context, user: User) {
        super.onLoginSuccess(context, user)
        Toast.makeText(context, context.getString(R.string.login_success), Toast.LENGTH_SHORT)
            .show()
        (context as AuthActivity).finish()
    }

    override fun onLoginFailed() {
        super.onLoginFailed()
        Toast.makeText(context, "Login Failed!", Toast.LENGTH_SHORT).show()
        UserInfo.clear()
    }

    fun onUserExists() {
        Toast.makeText(context, "This Registration Email Exist", Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun logout(context: Context) {
            UserInfo.saveNotification(context, false)
            Firebase.auth.signOut()
            FirebaseAuth.getInstance().signOut()
            LoginManager.getInstance().logOut()
            UserInfo.clear()
            UserInfo.clearSharedPref(context)
            GlobalScope.launch {
                ShopLexDataBase.getDatabase(context).clearAllTables()
            }
        }
    }
}