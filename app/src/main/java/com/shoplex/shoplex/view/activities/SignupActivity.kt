package com.shoplex.shoplex.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase
import com.google.type.LatLng
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivitySignupBinding
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.pojo.Location
import com.shoplex.shoplex.model.pojo.User
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var ref: DocumentReference
    private val MAPS_CODE = 202
    private val OPEN_GALLERY_CODE = 200
    private var imageUser: Uri? = null
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ref = FirebaseReferences.usersRef.document()

        binding.edPhone.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    binding.tiPhone.setHint(getString(R.string.phone))
                } else {
                    binding.tiPhone.setHint(getString(R.string.phone_number_hint))
                }
            }
        })
        binding.btnSignup.setOnClickListener {
            val name = binding.edName.text.toString()
            val email = binding.edEmail.text.toString()
            val phone = binding.edPassword.text.toString()
            val img =
                "https://img.etimg.com/thumb/width-1200,height-900,imgsize-122620,resizemode-1,msid-75214721/industry/services/retail/future-group-negotiates-rents-for-its-1700-stores.jpg"

            if (checkEditText()) {
                createAccount(email, binding.edPassword.text.toString())
                user =
                    User(
                        ref.id,
                        name,
                        email,
                        Location(0.0, 0.0),
                        "Address",
                        phone,
                        img,
                        arrayListOf(),
                        arrayListOf()
                    )
                addUser(user)
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
        binding.btnLocation.setOnClickListener {
            startActivityForResult(Intent(this, MapsActivity::class.java), MAPS_CODE)
        }
        binding.imgSignup.setOnClickListener {
            openGallary()

        }

        editTextChange()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MAPS_CODE) {
            if (resultCode == RESULT_OK) {
                val location: Parcelable? = data?.getParcelableExtra("Loc")
                if (location != null) {
                    // binding.tvLocation.text = getAddress(location as LatLng)
                }
            }
        } else if (requestCode == OPEN_GALLERY_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null || data.data == null) {
                    return
                }
                imageUser = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUser)
                    binding.imgSignup.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun openGallary() {
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        startActivityForResult(intent, OPEN_GALLERY_CODE)
    }

    fun editTextChange() {
        binding.edName.addTextChangedListener {
            binding.tiName.error = null
        }
        binding.edEmail.addTextChangedListener {
            binding.tiEmail.error = null
        }
        binding.edPassword.addTextChangedListener {
            binding.tiPassword.error = null
        }
        binding.edConfirmPassword.addTextChangedListener {
            binding.tiConfirmPassword.error = null
        }
        binding.edPhone.addTextChangedListener {
            binding.tiPhone.error = null
        }
    }


    fun addUser(user: User) {
        ref.set(user).addOnSuccessListener {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }

    fun checkEditText(): Boolean {
        when {
            binding.edName.length() == 0 -> binding.tiName.error = getString(R.string.Required)
            binding.edName.length() < 5 -> binding.tiName.error =
                getString(R.string.min_client_name_err)
            binding.edEmail.length() == 0 -> binding.tiEmail.error = getString(R.string.Required)
            !isEmailValid(binding.edEmail.text.toString()) -> binding.tiEmail.error =
                getString(R.string.require_email)

            binding.edPassword.length() == 0 -> binding.tiPassword.error =
                getString(R.string.Required)
            binding.edPassword.length() < 8 -> binding.tiPassword.error =
                getString(R.string.min_password_err)
            binding.edConfirmPassword.length() == 0 -> binding.tiConfirmPassword.error =
                getString(R.string.Required)
            binding.edConfirmPassword.text.toString() != binding.edPassword.text.toString() -> binding.tiConfirmPassword.error =
                getString(
                    R.string.not_match
                )

            !isValidMobile(binding.edPhone.text.toString()) -> binding.tiPhone.error =
                getString(R.string.Required)
            else -> return true
        }
        return false
    }

    //Email Validation
    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun isValidMobile(phone: String): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone.length > 11 && phone.length <= 13
        } else false
    }
}
