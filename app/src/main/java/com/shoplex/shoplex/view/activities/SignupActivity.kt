package com.shoplex.shoplex.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng

import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivitySignupBinding
import com.shoplex.shoplex.model.enumurations.LocationAction
import com.shoplex.shoplex.model.pojo.Location
import com.shoplex.shoplex.viewmodel.AuthVM
import com.shoplex.shoplex.viewmodel.AuthVMFactory
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var authVM: AuthVM
    private val OPEN_GALLERY_CODE = 200
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authVM = ViewModelProvider(this, AuthVMFactory(this)).get(AuthVM::class.java)
        binding.userData = authVM
        binding.btnSignup.setOnClickListener {
            // val img = "https://img.etimg.com/thumb/width-1200,height-900,imgsize-122620,resizemode-1,msid-75214721/industry/services/retail/future-group-negotiates-rents-for-its-1700-stores.jpg"
            // authVM.user.value!!.image = img
            if (validateInput()) {
                authVM.createAccount()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
        binding.btnLocation.setOnClickListener {
            startActivityForResult(
                Intent(this, MapsActivity::class.java)
                    .apply {
                        putExtra(MapsActivity.LOCATION_ACTION, LocationAction.Add.name)
                    }, MapsActivity.MAPS_CODE
            )
        }

        binding.imgSignup.setOnClickListener {
            openGallary()
        }
        binding.edPhone.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    binding.tiPhone.setHint(getString(R.string.phone))
                } else {
                    binding.tiPhone.setHint(getString(R.string.phone_hint))
                }
            }
        })
        onEditTextChanged()
    }

    private fun openGallary() {
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.type = "image/*"
        startActivityForResult(intent, OPEN_GALLERY_CODE)
    }

    private fun onEditTextChanged(){
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
        binding.tvLocation.addTextChangedListener{
            binding.tvLocation.error = null
        }
    }

    private fun validateInput(): Boolean {
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

            binding.edPhone.text.toString().isEmpty() -> binding.tiPhone.error =
                getString(R.string.Required)

            !isValidMobile(binding.edPhone.text.toString()) -> binding.tiPhone.error =
                "Please Enter Valid Mobile"

            binding.tvLocation.text.trim()
                .toString() == "Egypt" || binding.tvLocation.text.length < 2 -> binding.tvLocation.error =
                "Please Select valid location!"




            authVM.user.value?.image.isNullOrEmpty() -> Toast.makeText(
                this,
                "Please, Choose Image",
                Toast.LENGTH_SHORT
            ).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MapsActivity.MAPS_CODE) {
            if (resultCode == RESULT_OK) {
                val location: LatLng? = data?.getParcelableExtra(MapsActivity.LOCATION)
                val address: String? = data?.getStringExtra(MapsActivity.ADDRESS)
                if (location != null) {
                    binding.tvLocation.text = address
                    authVM.user.value!!.address = address!!
                    authVM.user.value!!.location = Location(location.latitude, location.longitude)
                }
            }
        } else if (requestCode == OPEN_GALLERY_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null || data.data == null) {
                    return
                }
                imageUri = data.data
                authVM.user.value!!.image = imageUri.toString()
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    binding.imgSignup.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
