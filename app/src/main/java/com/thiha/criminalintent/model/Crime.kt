package com.thiha.criminalintent.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/18/2020
 */
@Entity(tableName = "crime_table")
data class Crime(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var title: String,
    @TypeConverters(Converters::class)
    var date: Date,
    var solved: Boolean,
    var requiredPolice: Boolean,
    var suspect: String
)