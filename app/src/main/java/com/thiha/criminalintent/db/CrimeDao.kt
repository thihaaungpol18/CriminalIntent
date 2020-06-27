package com.thiha.criminalintent.db

import androidx.lifecycle.LiveData
import androidx.room.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/27/2020
 */
@Dao
interface CrimeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crime: Crime)

    @Delete
    suspend fun deleteCrime(crime:Crime)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(crime: Crime)

    @Query("SELECT * FROM crime_table ORDER BY id DESC")
    fun getAllCrimes(): LiveData<List<Crime>>

    @Query("DELETE FROM crime_table")
    suspend fun deleteAllCrimes()


}