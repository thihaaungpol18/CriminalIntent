package com.thiha.criminalintent.db

import androidx.room.TypeConverter
import java.util.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/27/2020
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}