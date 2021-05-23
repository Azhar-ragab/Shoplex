package com.shoplex.shoplex.view.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityFilterBinding
import java.text.NumberFormat
import java.util.*


class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBarFilter)
        supportActionBar?.apply {
            title = getString(R.string.filter)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        }
        if (getSupportActionBar() != null){
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        }

        //format range slider Label
        binding.rsPrice.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("EGP")
            format.format(value.toDouble())
        }

        binding.toggleBtnPrice.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(isChecked){
                when(checkedId){
                    R.id.btnLowPrice -> Toast.makeText(this,"Low Price Selected",Toast.LENGTH_SHORT).show()
                    R.id.btnHighPrice -> Toast.makeText(this,"High Price Selected",Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnFilter.setOnClickListener {
            showBottomSheetDialog()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_shops)

        bottomSheetDialog.show()
    }
}