package com.thiha.criminalintent.model

/**
project: CriminalIntent
Created by : Thiha
date : 6/18/2020
 */
data class Crime(
    var mId: Int,
    var mTitle: String,
    var mDate: String,
    var mSolved: Boolean,
    var mRequiresPolice: Boolean
)