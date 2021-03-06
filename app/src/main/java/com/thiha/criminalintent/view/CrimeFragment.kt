package com.thiha.criminalintent.view

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.thiha.criminalintent.R
import com.thiha.criminalintent.model.Crime
import com.thiha.criminalintent.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_crime.*
import java.io.File
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
    private var photoFile: File? = null
    private var globalYear: Int? = null
    private var globalMonth: Int? = null
    private var globalDay: Int? = null
    private var globalHour: Int? = null
    private var globalMinute: Int? = null
    private var globalDate: Date? = null
    private var globalSuspectPhone: String? = null

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

            currentPosition = arguments?.getInt(ARG_CRIME_ID)

            populateData()

            btn_done.text = getString(R.string.update)

            btn_done.setOnClickListener {
                insertCrime()
            }
        } else {
            btn_crime_date.setOnClickListener {
                openPickers()
            }


            btn_done.text = getString(R.string.save)

            btn_send_report.visibility = Button.GONE
            btn_choose_suspect.setOnClickListener {
                chooseSuspect()
            }
            btn_call.setOnClickListener {
                callSuspect()
            }

            btn_done.setOnClickListener {
                insertCrime()
            }
        }
    }

    private fun sendReport() {
        var i = Intent(Intent.ACTION_SEND)
        val crime = currentCrime!!
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_TEXT, getCrimeReport(crime))
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
        i = Intent.createChooser(i, getString(R.string.send_report_via))
        startActivity(i)
    }

    private fun populateData() {

        viewModel.allCrimes.observe(viewLifecycleOwner, Observer {
            if (currentPosition != -1) {
                currentCrime = it[currentPosition!!]

                globalDate = currentCrime!!.date

                globalSuspectPhone = currentCrime!!.suspect_ph

                photoFile = getPhotoFile(currentCrime!!)

                et_title.setText(currentCrime!!.title)

                btn_crime_date.text = formatDate(currentCrime!!.date)

                checkbox_crime_solved.isChecked = currentCrime!!.solved

                checkbox_required_police.isChecked = currentCrime!!.requiredPolice

                btn_choose_suspect.text = currentCrime!!.suspect

                if (currentCrime!!.requiredPolice) {
                    btn_call_police.visibility = View.VISIBLE
                }

                btn_done.setOnClickListener {
                    updateCrime()
                }

                btn_choose_suspect.setOnClickListener {
                    chooseSuspect()
                }
                btn_call.setOnClickListener {
                    callSuspect()
                }

                btn_send_report.setOnClickListener {
                    sendReport()
                }

                btn_crime_date.setOnClickListener {
                    openPickers()
                }

                btn_call_police.setOnClickListener { callPolice() }


                val pm = activity?.packageManager
                val canTakePhoto =
                    photoFile != null && Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(pm!!) != null
                if (!canTakePhoto) {
                    btnCamera.isEnabled = false
                } else {
                    btnCamera.setOnClickListener { onCameraClick() }
                }

            } else {
                currentCrime = null
            }
        })
    }

    private fun insertCrime() {

        if (btn_crime_date.text == getString(R.string.date)) {
            val snackbar =
                Snackbar.make(et_title, "Date Plz", Snackbar.LENGTH_SHORT)
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
                snackbar.setAction(getString(R.string.setDate)) { openPickers() }.show()
            }
            return
        }

        if (et_title.text.toString().trim().isNotEmpty()) {
            val result = viewModel.insert(
                Crime(
                    id = 0,
                    title = et_title.text.toString().trim(),
                    date = globalDate!!,
                    solved = checkbox_crime_solved.isChecked,
                    requiredPolice = (checkbox_required_police.isChecked),
                    suspect = btn_choose_suspect.text.toString(),
                    suspect_ph = globalSuspectPhone!!
                )
            )
            if (result.isActive || !result.isCancelled || result.isCompleted) {
                val snackbar =
                    Snackbar.make(et_title, "Data Inserted", Snackbar.LENGTH_SHORT)
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
        findNavController().navigate(R.id.goHome)
    }

    private fun updateCrime() {
        if (et_title.text.toString().trim().isNotEmpty()) {
            currentCrime!!.title = et_title.text.toString().trim()
            currentCrime!!.requiredPolice = checkbox_required_police.isChecked
            currentCrime!!.solved = checkbox_crime_solved.isChecked
            currentCrime!!.date = globalDate!!
            val result = viewModel.update(currentCrime!!)
            if (result.isCompleted || !result.isCancelled || result.isActive) {
                val snackBar = Snackbar.make(et_title, "Updated", Snackbar.LENGTH_SHORT)
                snackBar.setAction("Dismiss") { snackBar.dismiss() }.show()
                findNavController().navigate(R.id.goHome)
            } else {
                Snackbar.make(et_title, "Cannot Update", Snackbar.LENGTH_SHORT).show()
            }
        } else {
            et_title.error = "Crime Title Plz"
            et_title.requestFocus()
        }
    }

    private fun callSuspect() {
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel: $globalSuspectPhone "))
        startActivity(callIntent)
    }

    private fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("EEE, d MMM yyyy hh:mm aaa", Locale.US)
        return formatter.format(date)
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

        val datePickerDialog: DatePickerDialog
        val timePickerDialog: TimePickerDialog
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        timePickerDialog = TimePickerDialog(
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

                globalDate = date
                btn_crime_date.text = formatDate(date)

            },
            c.get(Calendar.HOUR),
            c.get(Calendar.MINUTE),
            false
        )

        datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, setYear, monthOfYear, dayOfMonth ->
                globalYear = setYear
                globalMonth = monthOfYear
                globalDay = dayOfMonth

                if (!timePickerDialog.isShowing) {
                    timePickerDialog.show()
                }
            },
            year,
            month,
            day
        )


        if (!datePickerDialog.isShowing) {
            datePickerDialog.show()
        }
    }

    private fun callPolice() {
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel: 199"))
        startActivity(callIntent)
    }

    private fun getPhotoFile(crime: Crime): File {
        val fileDir = requireContext().filesDir
        return File(fileDir, crime.getFileName())
    }

    private fun onCameraClick() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri = FileProvider.getUriForFile(
            requireActivity(),
            "com.thiha.criminalintent.fileprovider",
            photoFile!!
        )

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

        val resolveInfo = requireActivity().packageManager.queryIntentActivities(
            cameraIntent,
            PackageManager.MATCH_DEFAULT_ONLY
        )

        for (i in resolveInfo) {
            requireActivity().grantUriPermission(
                i.activityInfo.toString(),
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }

        startActivityForResult(cameraIntent, PHOTO_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CONTACT_REQUEST && resultCode == Activity.RESULT_OK && resultCode != Activity.RESULT_CANCELED && data != null) {

            Log.i("MyLog", "returnContactData : ${data.data} \n\n $data ")

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    PERMISSION_REQUEST
                )
            } else {

                var name = ""
                var phone = ""

                val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

                val cursor = requireContext().contentResolver.query(
                    data.data!!,
                    projection,
                    null,
                    null,
                    null
                )

                if (cursor != null) {

                    if (cursor.count == 0) {
                        return
                    } else {
                        if (cursor.moveToFirst()) {
                            name = cursor.getString(0)
                            cursor.close()
                        }
                    }
                }
                val selection = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} =?"

                val selectionArgs = arrayOf(name)

                val secondCursor = requireContext().contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(ContactsContract.CommonDataKinds.Phone.DATA4),
                    selection,
                    selectionArgs,
                    null
                )

                if (secondCursor != null) {
                    if (secondCursor.count == 0) {
                        return
                    } else {
                        if (secondCursor.moveToFirst()) {
                            phone = if (!secondCursor.getString(0).isNullOrEmpty()) {
                                secondCursor.getString(0)
                            } else {
                                ""
                            }
                            secondCursor.close()
                        }
                    }
                }
                btn_choose_suspect.text = name
                currentCrime?.suspect = name
                currentCrime?.suspect_ph = phone
                globalSuspectPhone = phone
            }
        }

        if (requestCode == PHOTO_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
//            val bitmap = BitmapFactory.decodeFile(photoFile?.path)
            Glide.with(requireContext()).load(photoFile?.path).into(ivProfile)
        }

    }

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val CONTACT_REQUEST = 3
        private const val PHOTO_REQUEST = 2
        private const val PERMISSION_REQUEST = 1
        fun newInstance(crimeID: Int): Fragment {
            val bundle = Bundle()
            bundle.putInt(ARG_CRIME_ID, crimeID)
            val fragment = CrimeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}