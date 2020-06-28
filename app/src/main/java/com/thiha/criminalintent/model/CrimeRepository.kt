package com.thiha.criminalintent.model

import androidx.lifecycle.LiveData

/**
project: CriminalIntent
Created by : Thiha
date : 6/27/2020
 */
class CrimeRepository(private val crimeDao: CrimeDao) {

    val allCrimes: LiveData<List<Crime>> = crimeDao.getAllCrimes()

    suspend fun insert(crime: Crime) {
        crimeDao.insert(crime)
    }

    suspend fun delete(crime: Crime) {
        crimeDao.deleteCrime(crime)
    }

    suspend fun update(crime: Crime) {
        crimeDao.update(crime)
    }

    suspend fun deleteAllCrimes() {
        crimeDao.deleteAllCrimes()
    }

}