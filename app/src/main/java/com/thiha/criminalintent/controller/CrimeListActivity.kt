package com.thiha.criminalintent.controller

import androidx.fragment.app.Fragment

class CrimeListActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return CrimeListFragment()
    }
}