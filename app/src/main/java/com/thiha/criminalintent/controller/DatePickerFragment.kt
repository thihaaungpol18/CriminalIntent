package com.thiha.criminalintent.controller

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.thiha.criminalintent.R
import kotlinx.android.synthetic.main.dialog_date.*
import java.util.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/21/2020
 */
class DatePickerFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()
        calendar.time = Date()
//        = arguments?.getSerializable(ARG_DATE) as Date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        dateToSend = GregorianCalendar(year, month, day).time
        val v = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_date, null)
        val datePicker = v.findViewById(R.id.id_datePicker) as DatePicker
        datePicker.init(year, month, day, null)

        return AlertDialog.Builder(requireContext())
            .setView(v)
            .setTitle(R.string.date_picker_title)
            .setPositiveButton(
                "Yes"
            ) { _, _ ->
                sendResult(
                    GregorianCalendar(
                        datePicker.year,
                        datePicker.month,
                        datePicker.dayOfMonth
                    ).time
                )
            }.create()
    }

    private fun sendResult(date: Date) {
        val intent = Intent()
        intent.putExtra(TAG, date)
        targetFragment
//        targetFragment!!.startActivity(intent)
//        targetFragment!!.onActivityResult(targetRequestCode, REQUEST_DATE, intent)
    }

    companion object {
        private const val ARG_DATE = "date"
        private const val REQUEST_DATE = 0
        private const val TAG = "DatePickerFragment"
        var dateToSend: Date? = null

        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)
            val fragment = DatePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
