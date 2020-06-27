package com.thiha.criminalintent.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.thiha.criminalintent.R


class CrimePagerActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crime, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
//          !  CrimeLab.deleteCrime(CrimeFragment.currentCrime).also {
//                Snackbar.make(btn_crime_date, "Deleted", Snackbar.LENGTH_LONG).show()
//            }
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
    }

}