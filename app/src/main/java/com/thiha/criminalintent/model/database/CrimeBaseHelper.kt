package com.thiha.criminalintent.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.thiha.criminalintent.model.database.CrimeDbSchema.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/22/2020
 */
class CrimeBaseHelper(context: Context?) :
    SQLiteOpenHelper(context, "crimeBase.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(
            "create table ${CrimeTable.NAME} (_id integer primary key autoincrement,${CrimeTable.Cols.UUID},${CrimeTable.Cols.TITLE},${CrimeTable.Cols.DATE},${CrimeTable.Cols.SOLVED})"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}