package com.thiha.criminalintent.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
project: CriminalIntent
Created by : Thiha
date : 6/27/2020
 */
@Database(entities = [Crime::class], version = 1, exportSchema = false)
abstract class CrimeDatabase : RoomDatabase() {
    abstract fun crimeDao(): CrimeDao

    companion object {
        @Volatile
        private var INSTANCE: CrimeDatabase? = null

        fun getInstance(context: Context): CrimeDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CrimeDatabase::class.java,
                    "crime_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}