package com.thiha.criminalintent.controller

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thiha.criminalintent.R
import com.thiha.criminalintent.model.Crime
import com.thiha.criminalintent.model.CrimeLab
import kotlinx.android.synthetic.main.fragment_crime.*
import java.text.DateFormat
import java.text.SimpleDateFormat
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
            it.isEnabled = false
        }

        val uuid = arguments?.getInt(ARG_CRIME_ID)
        val currentCrime = CrimeLab.getCrime(uuid!!)

        et_title.setText(currentCrime.mTitle)
        btn_crime_date.text = currentCrime.mDate
        checkbox_crime_solved.isChecked = currentCrime.mSolved
        if (currentCrime.mRequiresPolice) {
            btn_call_police.visibility = View.VISIBLE
        }
        btn_done.setOnClickListener {
            CrimeLab.addCrime(
                Crime(
                    mId = CrimeLab.getCrimes().size + 1,
                    mTitle = et_title.text.toString().trim(),
                    mDate = "${SimpleDateFormat(
                        "EEE",
                        Locale.US
                    ).format(Date())},${DateFormat.getDateInstance().format(Date())}",
                    mSolved = checkbox_crime_solved.isChecked,
                    mRequiresPolice = (Random().nextInt(10) % 2 == 0)
                )
            )
            context?.startActivity(Intent(context, CrimeListActivity::class.java))
        }
    }

    companion object {

        private const val ARG_CRIME_ID = "crime_id"

        fun newInstance(crimeID: Int): Fragment {
            val bundle = Bundle()
            bundle.putInt(ARG_CRIME_ID, crimeID)
            val fragment = CrimeFragment()
            fragment.arguments = bundle
            return fragment
        }

    }
}