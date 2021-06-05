package com.shoplex.shoplex.room.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shoplex.shoplex.model.pojo.LastOrder


@Database(entities = [LastOrder::class], version = 1)
abstract class ShoplexDataBase : RoomDatabase(){

    abstract fun shoplexDoa() : ShoplexDoa

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
                    "shoplex_firebase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}