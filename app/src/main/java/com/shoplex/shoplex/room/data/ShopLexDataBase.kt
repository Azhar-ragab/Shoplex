package com.shoplex.shoplex.room.data

import android.content.Context
import androidx.room.*
import com.shoplex.shoplex.model.pojo.Message
import com.shoplex.shoplex.model.pojo.ProductCart
import com.shoplex.shoplex.model.pojo.ProductFavourite
import com.shoplex.shoplex.model.pojo.StoreLocationInfo


@Database(entities = [ProductCart::class, ProductFavourite::class, Message::class, StoreLocationInfo::class], version = 1)
@TypeConverters(Converter ::class)
abstract class ShopLexDataBase : RoomDatabase(){

    abstract fun shoplexDao() : ShopLexDao

    companion object{
        @Volatile
        private var INSTANCE : ShopLexDataBase? = null
        fun getDatabase(context: Context):ShopLexDataBase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShopLexDataBase::class.java,
                    "shoplex_Database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
