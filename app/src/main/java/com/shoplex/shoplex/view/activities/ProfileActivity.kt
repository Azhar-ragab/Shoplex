package com.shoplex.shoplex.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import com.google.type.LatLng
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val MAPS_CODE = 202

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarProfile)
        supportActionBar?.apply {
            title = getString(R.string.profile)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        }
        if (getSupportActionBar() != null){
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        }
        binding.btnLocation.setOnClickListener {
            startActivityForResult(Intent(this, MapsActivity::class.java), MAPS_CODE)

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
        }
    }
    fun getAddress(loc: LatLng): String{
        return "Address"
    }
}