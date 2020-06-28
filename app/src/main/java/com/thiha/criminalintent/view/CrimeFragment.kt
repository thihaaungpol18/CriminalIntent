package com.thiha.criminalintent.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.thiha.criminalintent.R
import com.thiha.criminalintent.model.Crime
import com.thiha.criminalintent.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_crime.*
import java.text.SimpleDateFormat
import java.util.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/18/2020
 */
class CrimeFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private var currentPosition: Int? = null
    private var currentCrime: Crime? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel = ListViewModel(requireActivity().application)

        viewModel = ViewModelProvider(requireActivity()).get(ListViewModel::class.java)
        if (arguments != null) {

            updateCrime()


            //        btn_crime_date.setOnClickListener {
//            val fm = parentFragmentManager
//            val datePickerFragment = DatePickerFragment.newInstance(currentCrime!!.mDate)
//            datePickerFragment.setTargetFragment(this, REQUEST_DATE)
//            datePickerFragment.show(fm, DIALOG_DATE)

//            val calendar = Calendar.getInstance()
//            calendar.time = Date()
//        = arguments?.getSerializable(ARG_DATE) as Date
//            val year = calendar.get(Calendar.YEAR)
//            val month = calendar.get(Calendar.MONTH)
//            val day = calendar.get(Calendar.DAY_OF_MONTH)
//            val v = LayoutInflater.from(requireContext())
//                .inflate(R.layout.dialog_date, null)
//            val datePicker = v.findViewById(R.id.id_datePicker) as DatePicker
//            datePicker.init(year, month, day, null)


//            AlertDialog.Builder(this.requireContext())
//                .setTitle(R.string.date_picker_title)
//                .setView(v)
//                .setPositiveButton(
//                    "Yes"
//                ) { _, _ ->
//                    currentCrime!!.mDate =
//                        GregorianCalendar(
//                            datePicker.year,
//                            datePicker.month,
//                            datePicker.dayOfMonth
//                        ).time
//                    btn_crime_date.text = DateToString().formatDate(currentCrime!!.mDate)
//                }
//                .create().show()
        } else {
            btn_done.setOnClickListener {
                insertCrime()
            }
        }

    }

    private fun updateCrime() {
        currentPosition = arguments?.getInt(ARG_CRIME_ID)

        viewModel.allCrimes.observe(viewLifecycleOwner, Observer {
            if (currentPosition != -1) {
                currentCrime = it[currentPosition!!]

                et_title.setText(currentCrime!!.title)

                val formatter = SimpleDateFormat("EEE, d MMM yyyy hh:mm aaa", Locale.US)
                val date = formatter.format(currentCrime!!.date)
                btn_crime_date.text = date

                checkbox_crime_solved.isChecked = currentCrime!!.solved
                checkbox_required_police.isChecked = currentCrime!!.requiredPolice
                if (currentCrime!!.requiredPolice) {
                    btn_call_police.visibility = View.VISIBLE
                }

                btn_done.setOnClickListener {
                    currentCrime!!.title = et_title.text.toString().trim()
                    currentCrime!!.requiredPolice = checkbox_required_police.isChecked
                    currentCrime!!.solved = checkbox_crime_solved.isChecked
                    currentCrime!!.date = Date()
                    val result = viewModel.update(currentCrime!!)
                    if (result.isCompleted || !result.isCancelled || result.isActive) {
                        val snackBar = Snackbar.make(et_title, "Updated", Snackbar.LENGTH_LONG)
                        snackBar.setAction("Dismiss") { snackBar.dismiss() }.show()
                        findNavController().navigate(R.id.goHome)
                    } else {
                        Snackbar.make(et_title, "Cannot Update", Snackbar.LENGTH_LONG).show()
                    }
                }
            } else {
                currentCrime = null
            }
        })
    }

    private fun insertCrime() {
        if (currentCrime != null) {
            viewModel.update(currentCrime!!)
        } else {
            if (et_title.text.toString().trim().isNotEmpty()) {
                val result = viewModel.insert(
                    Crime(
                        id = 0,
                        title = et_title.text.toString().trim(),
                        date = Date(),
                        solved = checkbox_crime_solved.isChecked,
                        requiredPolice = (checkbox_required_police.isChecked)
                    )
                )
                if (result.isActive || !result.isCancelled || result.isCompleted) {
                    val snackbar = Snackbar.make(et_title, "Data Inserted", Snackbar.LENGTH_SHORT)
                    snackbar.setAction("Dismiss") { snackbar.dismiss() }.show()
                } else {
                    val snackbar =
                        Snackbar.make(et_title, "Data Inserted Fail", Snackbar.LENGTH_SHORT)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        snackbar.setTextColor(
                            resources.getColor(
                                android.R.color.holo_red_dark,
                                null
                            )
                        )
                        snackbar.setActionTextColor(
                            resources.getColor(
                                android.R.color.holo_red_dark,
                                null
                            )
                        )
                        snackbar.setAction("Dismiss") { snackbar.dismiss() }.show()
                    }
                }
            } else {
                et_title.error = "Title Required"
                et_title.requestFocus()
                return
            }
        }
        findNavController().navigate(R.id.goHome)
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_DATE && resultCode == Activity.RESULT_OK) {
//            btn_crime_date.text = data?.getSerializableExtra("DatePickerFragment").toString()
//            currentCrime?.date =
//                (data?.getSerializableExtra("DatePickerFragment") as Date).toString()
//            Toast.makeText(this.context, "OK", Toast.LENGTH_SHORT).show()
//        }
//    }

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val TAG = "CrimeFragment"
        fun newInstance(crimeID: Int): Fragment {
            val bundle = Bundle()
            bundle.putInt(ARG_CRIME_ID, crimeID)
            val fragment = CrimeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}