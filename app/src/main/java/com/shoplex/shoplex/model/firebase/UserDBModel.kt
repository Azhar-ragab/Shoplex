package com.shoplex.shoplex.model.firebase


import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.interfaces.UserActionListener
import com.shoplex.shoplex.model.pojo.User

class UserDBModel(val listener: UserActionListener) {

    fun createAccount(user: User, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    addNewUser(user)
                } else {
                    listener.onAddNewUser(false)
                }
            }
    }

    fun addNewUser(user: User) {
        val ref: DocumentReference = FirebaseReferences.usersRef.document()
        user.userID = ref.id
        ref.set(user).addOnSuccessListener {
            listener.onAddNewUser(true)
        }.addOnFailureListener {
            listener.onAddNewUser(false)
        }
    }

    fun getUserByMail(userEmail: String, isFacebookLogin: Boolean = false) {
        FirebaseReferences.usersRef.whereEqualTo("email", userEmail).get().addOnSuccessListener {
            if (it.count() > 0) {
                val user: User = it.documents[0].toObject()!!
                listener.onUserInfoReady(user)
            } else {
                if (isFacebookLogin) {
                    listener.onNewFacebookAccountCreated()
                } else {
                    listener.onUserInfoFailed()
                }
            }
        }.addOnFailureListener {
            listener.onUserInfoFailed()
        }
    }
}