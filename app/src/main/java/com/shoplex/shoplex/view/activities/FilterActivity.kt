package com.shoplex.shoplex.view.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityFilterBinding


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