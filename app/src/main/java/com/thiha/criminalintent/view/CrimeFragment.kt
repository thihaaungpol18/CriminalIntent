package com.thiha.criminalintent.view

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
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

    private var globalYear: Int? = null
    private var globalMonth: Int? = null
    private var globalDay: Int? = null
    private var globalHour: Int? = null
    private var globalMinute: Int? = null
    private var globalDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ListViewModel::class.java)
        if (arguments != null) {

            updateCrime()

            viewModel.allCrimes.observe(viewLifecycleOwner, Observer { list ->
                btn_send_report.setOnClickListener {
                    var i = Intent(Intent.ACTION_SEND)
                    val crime = list[currentPosition!!]
                    i.type = "text/plain"
                    i.putExtra(Intent.EXTRA_TEXT, getCrimeReport(crime))
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
                    i = Intent.createChooser(i, getString(R.string.send_report_via))
                    startActivity(i)
                }
            })

            btn_done.setOnClickListener {
                insertCrime()
            }
        }

        btn_done.setOnClickListener {
            insertCrime()
        }

        btn_choose_suspect.setOnClickListener {
            chooseSuspect()
        }

        btn_crime_date.setOnClickListener {
            openPickers()
        }
    }

    private fun updateCrime() {
        currentPosition = arguments?.getInt(ARG_CRIME_ID)

        viewModel.allCrimes.observe(viewLifecycleOwner, Observer {
            if (currentPosition != -1) {
                currentCrime = it[currentPosition!!]

                globalDate = currentCrime!!.date

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
                    currentCrime!!.date = globalDate!!
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
                        date = globalDate!!,
                        solved = checkbox_crime_solved.isChecked,
                        requiredPolice = (checkbox_required_police.isChecked)
                        , suspect = btn_choose_suspect.text.toString()
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

    private fun getCrimeReport(crime: Crime): String {
        val solvedString = if (crime.solved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }
        val suspectString =
            if (crime.suspect == resources.getString(R.string.choose_suspect)) {
                resources.getString(R.string.crime_report_no_suspect)
            } else {
                resources.getString(R.string.crime_report_suspect, crime.suspect)
            }

        return resources.getString(
            R.string.crime_report,
            crime.title,
            crime.date,
            solvedString,
            suspectString
        )
    }

    private fun chooseSuspect() {

        val contactIntent =
            Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(contactIntent, CONTACT_REQUEST)
    }

    private fun openPickers() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                globalHour = hourOfDay
                globalMinute = minute

                val date = GregorianCalendar(
                    globalYear!!,
                    globalMonth!!,
                    globalDay!!,
                    globalHour!!,
                    globalMinute!!
                ).time

                val formatter = SimpleDateFormat("EEE, d MMM yyyy hh:mm aaa", Locale.US)
                val formattedDate = formatter.format(date)
                globalDate = date
                btn_crime_date.text = formattedDate
            },
            c.get(Calendar.HOUR),
            c.get(Calendar.MINUTE),
            false
        )

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, setYear, monthOfYear, dayOfMonth ->
                globalYear = setYear
                globalMonth = monthOfYear
                globalDay = dayOfMonth

                timePickerDialog.show()

            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CONTACT_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val contactUri = data.data
            val names = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
            val cursor = requireContext().contentResolver.query(
                contactUri!!, names,
                null, null, null
            )
            try {
                if (cursor != null && cursor.count != 0) {
                    cursor.moveToFirst()

                    val numberIndex =
                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

                    val nameIndex =
                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

                    val number = cursor.getString(numberIndex)
                    val name = cursor.getString(nameIndex)

                    Log.i("MyLog", " number : $number $name ")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                cursor?.close()
            }
        }
    }

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val CONTACT_REQUEST = 3
        private const val DATETIME_REQUEST = 2
        fun newInstance(crimeID: Int): Fragment {
            val bundle = Bundle()
            bundle.putInt(ARG_CRIME_ID, crimeID)
            val fragment = CrimeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}