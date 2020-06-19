package com.thiha.criminalintent.model

import android.content.Context
import java.util.*
import kotlin.collections.ArrayList

object CrimeLab {
    private var mCrimes: ArrayList<Crime> = ArrayList()

    init {
        for (i in 1..100) {
            mCrimes.add(
                Crime(
                    UUID.randomUUID(),
                    "Crime #$i",
                    Date(),
                    mSolved = (i % 2 == 0)
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

    fun getCrime(id: Int): Crime {
        return mCrimes[id]
    }
}