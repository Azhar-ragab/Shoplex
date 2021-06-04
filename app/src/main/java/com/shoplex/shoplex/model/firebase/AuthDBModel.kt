package com.shoplex.shoplex.model.firebase


import android.content.Context
import android.widget.Toast
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.model.enumurations.AuthType
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.interfaces.UserActionListener
import com.shoplex.shoplex.model.pojo.Location
import com.shoplex.shoplex.model.pojo.User

class AuthDBModel(val listener: UserActionListener, val context: Context) {
    fun loginWithEmail(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getUserByMail(email, AuthType.Email)
                } else {
                    listener.onLoginFailed()
                }
            }
    }

    fun loginWithFacebook(accessToken: AccessToken) {
        val authCredential: AuthCredential = FacebookAuthProvider.getCredential(accessToken.token)
        Firebase.auth.signInWithCredential(authCredential).addOnCompleteListener { task ->
            if (task.isSuccessful) run {
                val user: FirebaseUser = Firebase.auth.currentUser
                getUserByMail(user.email, AuthType.Facebook)
            } else {
                Toast.makeText(context, "couldn`t register to firebase", Toast.LENGTH_SHORT).show()

            }
        }
    }

    fun createEmailAccount(user: User, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    addNewUser(user)
                } else {
                    listener.onAddNewUser(null)
                    Toast.makeText(context, "Auth Failed!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun addNewUser(user: User) {
        val ref: DocumentReference = FirebaseReferences.usersRef.document()
        user.userID = ref.id
        ref.set(user).addOnSuccessListener {
            listener.onAddNewUser(user)
            Toast.makeText(context, "Success to create your account!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            listener.onAddNewUser(null)
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    fun addSocialUser(authType: AuthType) {
        val ref: DocumentReference = FirebaseReferences.usersRef.document()
        val currentUser: FirebaseUser = Firebase.auth.currentUser

        val user = User(
            ref.id,
            currentUser.displayName,
            currentUser.email,
            Location(0.0, 0.0),
            "Address",
            currentUser.phoneNumber ?: "",
            currentUser.photoUrl.toString(),
            arrayListOf(),
            arrayListOf(),
            authType
        )

        ref.set(user).addOnSuccessListener {
            listener.onAddNewUser(user)
            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            listener.onAddNewUser(null)
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserByMail(userEmail: String, authType: AuthType) {
        FirebaseReferences.usersRef.whereEqualTo("email", userEmail)
            .whereEqualTo("authType", authType).get()
            .addOnSuccessListener {
                var user: User?
                when {
                    it.documents.count() > 0 -> {
                        user = it.documents[0].toObject()!!
                        listener.onLoginSuccess(user)
                    }
                    authType != AuthType.Email -> {

                        addSocialUser(authType)

                    }

                    else -> {
                        listener.onLoginFailed()
                    }
                }
            }.addOnFailureListener {
                listener.onLoginFailed()
            }
    }ع
}