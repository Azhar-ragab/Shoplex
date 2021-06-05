package com.shoplex.shoplex.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shoplex.shoplex.R
// Import fawryplugin
import com.emeint.android.fawryplugin.Plugininterfacing.FawrySdk;
import com.emeint.android.fawryplugin.Plugininterfacing.PayableItem;
import com.emeint.android.fawryplugin.interfaces.FawrySdkCallback;
import com.emeint.android.fawryplugin.views.cutomviews.FawryButton
import com.shoplex.shoplex.databinding.ActivityFawryBinding

class FawryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFawryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fawry)
        FawrySdk.init(FawrySdk.Styles.STYLE1)
//        var items:ArrayList<PayableItem> = ArrayList()
//          items.add("Item SKU in String","Item description in String",
//            "Item quantity in String","Item price in String")

        var checkout: FawryButton = FawryButton(this)
       // binding.checkoutFawry
    }
}