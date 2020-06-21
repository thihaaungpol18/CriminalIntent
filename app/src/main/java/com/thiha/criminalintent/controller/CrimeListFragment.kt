package com.thiha.criminalintent.controller

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.thiha.criminalintent.R
import com.thiha.criminalintent.model.CrimeAdapter
import com.thiha.criminalintent.model.OnClickListener
import kotlinx.android.synthetic.main.fragment_list_crime.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/18/2020
 */
class CrimeListFragment : Fragment(),
    OnClickListener {
    private lateinit var adapter: CrimeAdapter
    private var pressedItem: Int? = null
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

        adapter = CrimeAdapter(this)
        id_recyclerView.adapter = adapter
    }

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
    }

    override fun onClick(position: Int) {
        pressedItem = position
        val intent = Intent(context, CrimePagerActivity::class.java)
        intent.putExtra(ARG_CRIME_ID, position)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        adapter.notifyItemChanged(pressedItem!!)
    }

}