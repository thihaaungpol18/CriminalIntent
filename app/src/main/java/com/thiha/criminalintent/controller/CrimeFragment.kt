package com.thiha.criminalintent.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thiha.criminalintent.model.Crime
import com.thiha.criminalintent.R
import kotlinx.android.synthetic.main.fragment_crime.*
import java.util.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/18/2020
 */
class CrimeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //For Date Btn
        btn_crime_date.also {
            it.text = Date().toString()
            it.isEnabled = false
        }


        btn_done.setOnClickListener {
            Crime(
                UUID.randomUUID(),
                et_title.text.toString().trim(),
                Date(),
                checkbox_crime_solved.isChecked
            )
        }
    }
}