package com.shoplex.shoplex.model.extra

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner

class ArchLifecycleApp : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        if (UserInfo.userID != null) {
            FirebaseReferences.chatRef.whereEqualTo("userID", UserInfo.userID!!).get().addOnSuccessListener {
                for(ref in it.documents)
                    ref.reference.update("isClientOnline", true)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        if (UserInfo.userID != null) {
            FirebaseReferences.chatRef.whereEqualTo("userID", UserInfo.userID!!).get().addOnSuccessListener {
                for(ref in it.documents)
                    ref.reference.update("isClientOnline", false)
            }
        }
    }
}