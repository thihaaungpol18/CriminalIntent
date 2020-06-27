package com.thiha.criminalintent.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.thiha.criminalintent.R
import com.thiha.criminalintent.db.Crime
import com.thiha.criminalintent.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list_crime.*
import java.util.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/18/2020
 */
class CrimeListFragment : Fragment(), CrimeAdapter.OnClickListener {
    private lateinit var adapter: CrimeAdapter
    private lateinit var viewModel: ListViewModel

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

        viewModel = ViewModelProvider(this@CrimeListFragment).get(ListViewModel::class.java)

        id_recyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        adapter = CrimeAdapter(this)
        id_recyclerView.adapter = adapter

        view.findViewById<FloatingActionButton>(R.id.id_fab).setOnClickListener {
            findNavController().navigate(R.id.toDetail)
        }

        val editText = EditText(this.requireContext())
        editText.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        viewModel.allCrimes.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.setCrimes(it)
            if (it.isEmpty()) {
                AlertDialog.Builder(this.requireContext()).setTitle("Add One Crime")
                    .setMessage("There is no crimes yet!")
                    .setView(editText)
                    .setPositiveButton("Ok") { _: DialogInterface, _: Int ->
                        if (editText.text.toString().trim().isNotEmpty()) {
                            viewModel.insert(
                                Crime(
                                    0,
                                    editText.text.toString().trim(),
                                    Date(),
                                    solved = false,
                                    requiredPolice = true
                                )
                            )
                        } else {
                            editText.error = "Enter Something"
                            editText.requestFocus()
                        }
                    }.show()

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_crime_list, menu)
        val subtitleItem = menu.findItem(R.id.menu_show_sub)
//        if (CrimeListActivity.mSubtitleVisible == true) {
//            subtitleItem.setTitle(R.string.hide_subtitle)
//        } else {
//            subtitleItem.setTitle(R.string.show_subtitle)
//        }
    }

    override fun onClick(position: Int) {
        val bundle = bundleOf("clickPosition" to position)
        findNavController().navigate(R.id.toPagerFrag, bundle)
    }
}