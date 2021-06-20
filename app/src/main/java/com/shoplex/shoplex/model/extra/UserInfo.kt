package com.shoplex.shoplex.model.extra

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shoplex.shoplex.model.pojo.Location
import com.shoplex.shoplex.model.pojo.NotificationToken
import com.shoplex.shoplex.model.pojo.RecentVisit
import java.lang.reflect.Type
import java.util.*

object UserInfo {
    private const val SHARED_USER_INFO = "USER_INFO"
    var userID: String? = null
    var name: String = ""
    var email: String = ""
    var image: String? = null
    var location: Location = Location(0.0, 0.0)
    var address: String = ""
    var phone: String? = null
    var lang: String = "en"

    fun updateTokenID() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) return@OnCompleteListener

            val token = task.result
            val notificationToken = NotificationToken(this.userID!!, token.toString())
            FirebaseReferences.notificationTokensRef.document(this.userID!!).set(notificationToken)
        })
    }

    fun saveUserInfo(context: Context) {
        context.getSharedPreferences(SHARED_USER_INFO, Context.MODE_PRIVATE).edit()
            .putString("userID", userID)
            .putString("name", name)
            .putString("email", email)
            .putString("image", image)
            .putString("location", Gson().toJson(location))
            .putString("address", address)
            .putString("phone", phone)
            .apply()
    }

    fun readUserInfo(context: Context) {
        lang =
            context.getSharedPreferences("LANG", Context.MODE_PRIVATE).getString("Language", "en")
                ?: "en"
        val sharedPref = context.getSharedPreferences(SHARED_USER_INFO, Context.MODE_PRIVATE)
        userID = sharedPref.getString("userID", null)
        if (userID == null)
            return
        name = sharedPref.getString("name", "")!!
        email = sharedPref.getString("email", "")!!
        image = sharedPref.getString("image", "")
        val locationType: Type = object : TypeToken<Location>() {}.type
        location =
            Gson().fromJson(sharedPref.getString("location", null), locationType) ?: Location()
        address = sharedPref.getString("address", "")!!
        phone = sharedPref.getString("phone", "")
    }

    fun clearSharedPref(context: Context) {
        context.getSharedPreferences(SHARED_USER_INFO, Context.MODE_PRIVATE).edit()
            .remove("name")
            .remove("email")
            .remove("image")
            .remove("location")
            .remove("address")
            .remove("phone")
            .apply()
    }

    fun saveNotification(context: Context, value: Boolean) {
        FirebaseReferences.notificationTokensRef.document(userID!!)
            .update("notification", value)
        context.getSharedPreferences(SHARED_USER_INFO, Context.MODE_PRIVATE).edit()
            .putBoolean("notification", value).apply()
    }

    fun readNotification(context: Context): Boolean {
        val shared = context.getSharedPreferences(
            SHARED_USER_INFO,
            Context.MODE_PRIVATE
        )
        return shared.getBoolean("notification", true)
    }

    fun saveToRecentVisits() {
        FirebaseReferences.recentVisits.add(RecentVisit())
    }

    fun clear() {
        this.userID = null
        this.name = ""
        this.email = ""
        this.image = null
        this.location = Location(0.0, 0.0)
        this.address = ""
        this.phone = null
    }

    fun isFirstTime(context: Context): Boolean {
        if (context.getSharedPreferences(context.packageName, AppCompatActivity.MODE_PRIVATE).getBoolean("firstRun", true)) {
            context.getSharedPreferences(context.packageName, AppCompatActivity.MODE_PRIVATE).edit().putBoolean("firstRun", false).apply()
            return true
        }
        return false
    }
}