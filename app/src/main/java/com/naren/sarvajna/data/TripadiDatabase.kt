package com.naren.sarvajna.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(Tripadi::class), version = 1)
abstract class TripadiDatabase : RoomDatabase() {

    abstract fun tripadiDao() : TripadiDao

    companion object {
        private var INSTANCE : TripadiDatabase? = null

        fun getInstance(context : Context) : TripadiDatabase? {
            if(INSTANCE == null) {
                synchronized (TripadiDatabase :: class) {
                    if(INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context,TripadiDatabase::class.java, "1.db").build()
                    }
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}