package com.thiha.criminalintent.model

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/21/2020
 */
class DateToString {
    fun formatDate(date: Date): String {
        return "${SimpleDateFormat("EEE", Locale.US).format(date)},${DateFormat.getDateInstance()
            .format(date)}"
    }
}