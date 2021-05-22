package com.shoplex.shoplex.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivitySignupBinding
import com.shoplex.shoplex.model.pojo.User
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val database = Firebase.firestore
    private var client: User = User()
    private lateinit var pendingSellerRef: CollectionReference
    private val MAPS_CODE = 202
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSignup.setOnClickListener {
            client.image =
                "https://img.etimg.com/thumb/width-1200,height-900,imgsize-122620,resizemode-1,msid-75214721/industry/services/retail/future-group-negotiates-rents-for-its-1700-stores.jpg"
            client.name = binding.edName.text.toString()
            client.email = binding.edEmail.text.toString()
            client.phone = binding.edPhone.text.toString()
            client.date = Timestamp.now().toDate()

            if (checkEditText() == true) {
                addSeller(closeContextMenu())
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
        binding.btnLocation.setOnClickListener {
            startActivityForResult(Intent(this, HomeActivity::class.java), MAPS_CODE)
        }
    }
    //check EditText
    fun checkEditText(): Boolean {
        when {
            binding.edName.length() == 0 -> binding.edName.error = getString(R.string.Required)
            binding.edName.length() < 5 -> binding.edName.error =
                getString(R.string.min_client_name_err)
            binding.edEmail.length() == 0 -> binding.edEmail.error = getString(R.string.Required)
            isEmailValid(binding.edEmail.text.toString()) != true -> binding.edEmail.error =
                getString(
                    R.string.require_email
                )

            binding.edPassword.length() == 0 -> binding.edPassword.error =
                getString(R.string.Required)
            binding.edPassword.length() < 8 -> binding.edPassword.error =
                getString(R.string.min_password_err)
            binding.edConfirmPassword.length() == 0 -> binding.edConfirmPassword.error =
                getString(R.string.Required)
            binding.edConfirmPassword.text.toString()
                .equals(binding.edPassword.text.toString()) != true -> binding.edConfirmPassword.error =
                getString(
                    R.string.not_match
                )

            binding.edPhone.length() == 0 -> binding.edPhone.error = getString(R.string.Required)
            client.addresses.size == 0 || client.locations?.size == 0 -> Toast.makeText(this,"Choose Your Location",
                Toast.LENGTH_LONG).show()
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MAPS_CODE){
            if(resultCode == RESULT_OK){
                val location: Parcelable? = data?.getParcelableExtra("Loc")
                if(location != null) {
                    val address = getAddress(location as LatLng,this)
                    client.locations.add(location)
                    binding.tvLocation.text = address
                    client.addresses.add(address)
                }
            }
        }
    }
}