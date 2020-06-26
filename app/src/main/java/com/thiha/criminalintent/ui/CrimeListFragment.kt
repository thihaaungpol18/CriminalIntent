package com.thiha.criminalintent.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.thiha.criminalintent.R
import com.thiha.criminalintent.db.Crime
import com.thiha.criminalintent.model.CrimeAdapter
import com.thiha.criminalintent.model.OnClickListener
import kotlinx.android.synthetic.main.fragment_list_crime.*
import java.util.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/18/2020
 */
class CrimeListFragment : Fragment(), CrimeAdapter.OnClickListener {
    private lateinit var adapter: CrimeAdapter
    private var pressedItem: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_list_crime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id_recyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        adapter = CrimeAdapter(this)
        id_recyclerView.adapter = adapter
        if (CrimeLab.getCrimes().isEmpty()) {

            val editText = EditText(this.requireContext())
            editText.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            AlertDialog.Builder(this.requireContext()).setTitle("Add One Crime")
                .setMessage("There is no crimes yet!")
                .setView(editText)
                .setPositiveButton("Ok") { _: DialogInterface, _: Int ->
                    if (editText.text.toString().trim().isNotEmpty()) {
                        CrimeLab.addCrime(
                            Crime(
                                UUID.randomUUID(), editText.text.toString().trim(),
                                Date(), false, mRequiresPolice = true
                            )
                        )
                    } else {
                        editText.error = "Enter Something"
                        editText.requestFocus()
                    }
                }.create().show()
        }
    }

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
    }

    override fun onResume() {
        super.onResume()
        id_recyclerView?.adapter?.notifyDataSetChanged()
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