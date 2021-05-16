package com.shoplex.shoplex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup



class PropertyAdapter(private val properties: ArrayList<Property>, private val context: Context) :
    RecyclerView.Adapter<PropertyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameProp: TextView = view.findViewById(R.id.tvPropName)
        val chipValues: ChipGroup = view.findViewById(R.id.cgPropValue)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.property_item_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = properties[position]

        viewHolder.nameProp.text = item.name
        val inflater = LayoutInflater.from(context)
        for (i in item.values) {
            val chipItem = inflater.inflate(R.layout.chip_item, null, false) as Chip
            chipItem.text = i
            chipItem.setOnCloseIconClickListener {
                viewHolder.chipValues.removeView(it)
            }
            viewHolder.chipValues.addView(chipItem)
        }

    }

    override fun getItemCount(): Int {

        return properties.size
    }
}



