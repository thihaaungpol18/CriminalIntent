package com.thiha.criminalintent.model.database

/**
project: CriminalIntent
Created by : Thiha
date : 6/22/2020
 */
class CrimeDbSchema {

    object CrimeTable {
        const val NAME = "crimes"

        object Cols {
            const val UUID = "uuid"
            const val TITLE = "title"
            const val DATE = "date"
            const val SOLVED = "solved"
        }
    }
}