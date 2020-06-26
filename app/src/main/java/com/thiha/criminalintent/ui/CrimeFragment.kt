package com.thiha.criminalintent.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.thiha.criminalintent.R
import com.thiha.criminalintent.db.Crime
import com.thiha.criminalintent.model.DateToString
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

        val uuid = arguments?.getInt(ARG_CRIME_ID)
//        currentCrime = CrimeLab.getCrime(uuid!!)

        btn_crime_date.setOnClickListener {
//            val fm = parentFragmentManager
//            val datePickerFragment = DatePickerFragment.newInstance(currentCrime!!.mDate)
//            datePickerFragment.setTargetFragment(this, REQUEST_DATE)
//            datePickerFragment.show(fm, DIALOG_DATE)

            val calendar = Calendar.getInstance()
            calendar.time = Date()
//        = arguments?.getSerializable(ARG_DATE) as Date
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val v = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_date, null)
            val datePicker = v.findViewById(R.id.id_datePicker) as DatePicker
            datePicker.init(year, month, day, null)


            AlertDialog.Builder(this.requireContext())
                .setTitle(R.string.date_picker_title)
                .setView(v)
                .setPositiveButton(
                    "Yes"
                ) { _, _ ->
                    currentCrime!!.mDate =
                        GregorianCalendar(
                            datePicker.year,
                            datePicker.month,
                            datePicker.dayOfMonth
                        ).time
                    btn_crime_date.text = DateToString().formatDate(currentCrime!!.mDate)
                }.create().show()

        }
        et_title.setText(currentCrime!!.mTitle)
        btn_crime_date.text = DateToString().formatDate(currentCrime!!.mDate)
        checkbox_crime_solved.isChecked = currentCrime!!.mSolved
        if (currentCrime!!.mRequiresPolice) {
            btn_call_police.visibility = View.VISIBLE
        }

        btn_done.setOnClickListener {
            currentCrime = Crime(
                mId = UUID.randomUUID(),
                mTitle = et_title.text.toString().trim(),
                mDate = Date(),
                mSolved = checkbox_crime_solved.isChecked,
                mRequiresPolice = (Random().nextInt(10) % 2 == 0)
            )
            activity?.finish()
        }
    }

    override fun onPause() {
        super.onPause()
        CrimeLab.get(requireContext()).updateCrime(currentCrime!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_DATE && resultCode == Activity.RESULT_OK) {
            btn_crime_date.text = data?.getSerializableExtra("DatePickerFragment").toString()
            currentCrime?.mDate = data?.getSerializableExtra("DatePickerFragment") as Date
            Toast.makeText(this.context, "OK", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val DIALOG_DATE = "DialogDate"
        private const val REQUEST_DATE = 0
        var currentCrime: Crime? = null
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