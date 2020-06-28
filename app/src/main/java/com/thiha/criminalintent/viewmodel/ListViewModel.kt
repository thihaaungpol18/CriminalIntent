package com.thiha.criminalintent.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.thiha.criminalintent.model.Crime
import com.thiha.criminalintent.model.CrimeDatabase
import com.thiha.criminalintent.model.CrimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
project: CriminalIntent
Created by : Thiha
date : 6/27/2020
 */
class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val crimeRepository: CrimeRepository
    val allCrimes: LiveData<List<Crime>>

    init {
        val crimeDao = CrimeDatabase.getInstance(application).crimeDao()
        crimeRepository = CrimeRepository(crimeDao)
        allCrimes = crimeRepository.allCrimes
    }

    fun insert(crime: Crime) = viewModelScope.launch(Dispatchers.IO) {
        crimeRepository.insert(crime)
    }

    fun delete(crime: Crime) = viewModelScope.launch(Dispatchers.IO) {
        crimeRepository.delete(crime)
    }

    fun update(crime: Crime) = viewModelScope.launch(Dispatchers.IO) {
        crimeRepository.update(crime)
    }

    fun deleteAllCrimes() = viewModelScope.launch(Dispatchers.IO) {
        crimeRepository.deleteAllCrimes()
    }

}