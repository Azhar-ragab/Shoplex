package com.shoplex.shoplex.room.data

import android.content.Context
import androidx.room.*
import com.shoplex.shoplex.model.pojo.Message
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.ProductFavourite


@Database(entities = [ProductCart::class, ProductFavourite::class, Message::class], version = 1)
@TypeConverters(Converter ::class)
abstract class ShoplexDataBase : RoomDatabase(){

    abstract fun shoplexDao() : ShopLexDao

    companion object{
        @Volatile
        private var INSTANCE : ShoplexDataBase? = null
        fun getDatabase(context: Context):ShoplexDataBase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShoplexDataBase::class.java,
                    "shoplex_Database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
