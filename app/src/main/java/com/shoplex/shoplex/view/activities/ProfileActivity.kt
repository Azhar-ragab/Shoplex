package com.shoplex.shoplex.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.LatLng
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityProfileBinding
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Location
import com.shoplex.shoplex.model.pojo.User
import java.io.IOException


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val MAPS_CODE = 202
    private val OPEN_GALLERY_CODE = 200
    private lateinit var user: User
    private var filePath: Uri? = null
    private  var name:String=""
    private  var email:String=""
    private  var  phone: String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarProfile)
        supportActionBar?.apply {
            title = getString(R.string.profile)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            binding.tvLocation.text = UserInfo.address
            binding.edName.setText(UserInfo.name)

        }
        if (getSupportActionBar() != null){
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        }
        binding.edName.setText(UserInfo.name)
        binding.edEmail.setText(UserInfo.email)
        binding.edPhone.setText(UserInfo.phone)
        binding.btnSave.setOnClickListener{
            name = binding.edName.text.toString()
            email = binding.edEmail.text.toString()
            phone = binding.edPhone.text.toString()
            val user: User = User(UserInfo.userID!!, name ,email,UserInfo.location, UserInfo.address ,
                phone ,   UserInfo.image , UserInfo.favouriteList,
                UserInfo.cartList )

            addUser(user)

        }
        /*val userID: String = "",
        val name: String = "",
        val email: String = "",
        val location: Location =Location(0.0,0.0),
        var address: String = "",
        val phone: String = "",
        val image: String? = "",
        val favouriteList: ArrayList<String> = ArrayList(),
        val cartList: ArrayList<String> = ArrayList()*/





        binding.btnLocation.setOnClickListener {


            var intent: Intent = Intent(binding.root.context, MapsActivity::class.java)
            intent.putExtra("locationLat", 13.621085324664428)
            intent.putExtra("locationLang",123.21271363645793)
            startActivityForResult(intent,MAPS_CODE)
        }
        binding.imgUser.setOnClickListener {
            openGallery()
        }
    }
    fun addUser(user: User) {
        Firebase.firestore.collection("Users").document(UserInfo.userID!!).set(user)
            .addOnSuccessListener {
                Toast.makeText(this ,  "Done" ,Toast.LENGTH_SHORT).show()
            }


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MAPS_CODE){
            if(resultCode == RESULT_OK){
                val location: Parcelable? = data?.getParcelableExtra("Loc")
                if(location != null) {
                    binding.tvLocation.text = getAddress(location as LatLng)
                }
            }
        }else if (requestCode == OPEN_GALLERY_CODE){
           if (resultCode == RESULT_OK)   {
               if(data == null || data.data == null){
                   return
               }

               filePath = data.data
               try {
                   val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                   binding.imgUser.setImageBitmap(bitmap)
               } catch (e: IOException) {
                   e.printStackTrace()
               }
           }
        }

    }
    fun getAddress(loc: LatLng): String{
        return "Address"
    }
    private fun openGallery() {
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        startActivityForResult(intent, OPEN_GALLERY_CODE)
    }
}