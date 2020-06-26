package com.thiha.criminalintent.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.thiha.criminalintent.model.database.CrimeBaseHelper
import com.thiha.criminalintent.model.database.CrimeDbSchema.CrimeTable


object CrimeLab {
    private var db: CrimeBaseHelper? = null
    fun get(context: Context): CrimeLab {
        db = CrimeBaseHelper(context)
        return CrimeLab
    }

    fun getCrimes(): List<Crime> {
        return ArrayList()
    }

    fun addCrime(crime: Crime) {
        db!!.writableDatabase.insert(CrimeTable.NAME, null, getContentValues(crime))
    }

    fun updateCrime(crime: Crime) {
        val uuidString: String = crime.mId.toString()
        val values = getContentValues(crime)
        db!!.writableDatabase.update(
            CrimeTable.NAME, values,
            CrimeTable.Cols.UUID + " = ?", arrayOf(uuidString)
        )
    }

    fun queryCrimes(whereClause: String, whereArgs: Array<String>): Cursor {
        val cursor: Cursor = db!!.writableDatabase.query(
            CrimeTable.NAME,
            null,
            whereClause,
            whereArgs,
            null,
            null,
            null
        )
        return cursor
    }
//    fun getCrime(id: Int): Crime {
//
//    }

    fun deleteCrime(currentCrime: Crime?) {

    }

    private fun getContentValues(crime: Crime): ContentValues? {
        val values = ContentValues()
        values.put(
            CrimeTable.Cols.UUID,
            crime.mId.toString()
        )
        values.put(
            CrimeTable.Cols.TITLE,
            crime.mTitle
        )
        values.put(
            CrimeTable.Cols.DATE,
            crime.mDate.time
        )
        values.put(
            CrimeTable.Cols.SOLVED,
            if (crime.mSolved) 1 else 0
        )
        return values
    }
}