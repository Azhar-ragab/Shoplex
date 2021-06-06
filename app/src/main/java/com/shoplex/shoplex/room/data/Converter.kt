package com.shoplex.shoplex.room.data

import android.media.Image
import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.Property
import com.shoplex.shoplex.model.pojo.SpecialDiscount
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class Converter {
    @TypeConverter
    fun sdToString(specialDiscount: SpecialDiscount): String = Gson().toJson(specialDiscount)

    @TypeConverter
    fun stringToSd(string: String): SpecialDiscount = Gson().fromJson(string, SpecialDiscount::class.java)
    @TypeConverter
    fun productToString(product: Product): String = Gson().toJson(product)

    @TypeConverter
    fun stringToProduct(string: String): Product = Gson().fromJson(string, Product::class.java)
    @TypeConverter
    fun fromProperty(value: String?): ArrayList<Property?>? {
        val listType: Type = object : TypeToken<ArrayList<Property?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toProperty(list: ArrayList<Property?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
    @TypeConverter
    fun fromImage(value: String?): ArrayList<String?>? {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toImage(list: ArrayList<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
    @TypeConverter
    fun fromUri(value: String?): ArrayList<Uri?>? {
        val listType: Type = object : TypeToken<ArrayList<Uri?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toUri(list: ArrayList<Uri?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

}