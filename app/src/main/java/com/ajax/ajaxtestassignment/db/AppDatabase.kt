package com.ajax.ajaxtestassignment.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ajax.ajaxtestassignment.db.contacts.ContactsDao
import com.ajax.ajaxtestassignment.db.contacts.DbContact

@Database(entities = [DbContact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): ContactsDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}
