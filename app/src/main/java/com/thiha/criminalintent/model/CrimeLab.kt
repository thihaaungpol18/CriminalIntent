package com.thiha.criminalintent.model

import android.content.Context
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object CrimeLab {
    private var mCrimes: ArrayList<Crime> = ArrayList()

    init {
        for (i in 1..20) {
            mCrimes.add(
                Crime(
                    mId = i,
                    mTitle = "Crime #$i",
                    mSolved = (i % 2 == 0),
                    mDate = "${SimpleDateFormat(
                        "EEE",
                        Locale.US
                    ).format(Date())},${DateFormat.getDateInstance().format(Date())}",
                    mRequiresPolice = (i % 2 == 0)
                )
            )
        }
    }

    fun get(context: Context): CrimeLab {
        return CrimeLab
    }

    fun getCrimes(): List<Crime> {
        return mCrimes
    }

    fun addCrime(crime: Crime) {
        mCrimes.add(crime)
    }

    fun getCrime(id: Int): Crime {
        return mCrimes[id]
    }
}