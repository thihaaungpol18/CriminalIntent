package com.thiha.criminalintent.model

import java.util.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/18/2020
 */
data class Crime(var mId: UUID, var mTitle: String, var mDate: Date, var mSolved: Boolean) {
    init {
        mId = UUID.randomUUID()
        mDate = Date()
    }
}