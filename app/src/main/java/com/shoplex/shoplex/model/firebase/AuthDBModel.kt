package com.shoplex.shoplex.model.firebase


import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.facebook.AccessToken
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
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

    fun loginWithFacebook(accessToken: String) {
        val authCredential: AuthCredential = FacebookAuthProvider.getCredential(accessToken)
        Firebase.auth.signInWithCredential(authCredential).addOnCompleteListener { task ->
            if (task.isSuccessful) run {
                val user: FirebaseUser = Firebase.auth.currentUser
                getUserByMail(user.email, AuthType.Facebook)
            } else {
                Toast.makeText(context, "couldn't register to firebase", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser = Firebase.auth.currentUser
                    getUserByMail(user.email, AuthType.Google)
                } else {
                    Toast.makeText(context, "couldn't register to firebase", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun createEmailAccount(user: User, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    addNewUser(user)
                } else {
                    listener.onAddNewUser(context, null)
                    Toast.makeText(context, "Auth Failed!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addNewUser(user: User) {
        val ref: DocumentReference = FirebaseReferences.usersRef.document()
        user.userID = ref.id
        val img = user.image
        user.image = ""
        ref.set(user).addOnSuccessListener {
            listener.onAddNewUser(context, user)
            addImage(Uri.parse(img),user.userID)
            Toast.makeText(context, "Success to create your account!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            listener.onAddNewUser(context, null)
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun addImage(uri: Uri,userId : String) {
        val imgRef: StorageReference =
            FirebaseReferences.imagesUserRef.child(userId)

        imgRef.putFile(uri).addOnSuccessListener { _ ->
            imgRef.downloadUrl.addOnSuccessListener {
                //add Image to FireStorage
                FirebaseReferences.usersRef.document(userId).update("image",it.toString())
                //update profile
                val profileUpdates = userProfileChangeRequest {
                    photoUri = it
                }
                Firebase.auth.currentUser.updateProfile(profileUpdates)

            }
        }
    }

    private fun addSocialUser(authType: AuthType) {
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
            listener.onAddNewUser(context, user)
            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            listener.onAddNewUser(context, null)
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
                        listener.onLoginSuccess(context, user)
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
    }
}