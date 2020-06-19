package com.thiha.criminalintent.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.thiha.criminalintent.R
import kotlinx.android.synthetic.main.activity_fragment.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/19/2020
 */
abstract class SingleFragmentActivity : AppCompatActivity() {

    abstract fun createFragment(): Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        setSupportActionBar(id_toolbar)

        val fm: FragmentManager = supportFragmentManager
        var fragment: Fragment? = fm.findFragmentById(R.id.id_container)
        if (fragment == null) {
            fragment = createFragment()
            fm.beginTransaction()
                .add(R.id.id_container, fragment).commit()
        }

    }
}