package com.thiha.criminalintent.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
project: CriminalIntent
Created by : Thiha
date : 6/18/2020
 */
@Entity(tableName = "crime_table")
data class Crime(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var date: String,
    var solved: Boolean,
    var requiredPolice: Boolean
)