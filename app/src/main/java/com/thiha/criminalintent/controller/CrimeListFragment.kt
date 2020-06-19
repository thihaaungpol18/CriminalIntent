package com.thiha.criminalintent.controller

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.thiha.criminalintent.OnClickListener
import com.thiha.criminalintent.R
import com.thiha.criminalintent.model.Crime
import com.thiha.criminalintent.model.CrimeAdapter
import com.thiha.criminalintent.model.CrimeLab
import kotlinx.android.synthetic.main.fragment_list_crime.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/18/2020
 */
class CrimeListFragment : Fragment(), OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_crime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id_recyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

        list = CrimeLab.get(view.context).getCrimes()
        id_recyclerView.adapter = CrimeAdapter(list!!, this)
        Log.i(TAG, "onViewCreated: ${list.hashCode()}")
    }

    companion object {
        private const val TAG = "CrimeListFragment"
        var list: List<Crime>? = null
    }

    override fun onClick(position: Int) {
        Toast.makeText(activity, "item : $position", Toast.LENGTH_SHORT).show()
    }
}