package com.thiha.criminalintent.controller

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.thiha.criminalintent.R
import com.thiha.criminalintent.model.CrimeLab
import kotlinx.android.synthetic.main.activity_crime_pager.*


class CrimePagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)
        val mCrimes = CrimeLab.get(this).getCrimes()
        val adapter = PagerAdapter(this@CrimePagerActivity, mCrimes.size)
        id_viewpager.adapter = adapter
        val i = intent.getIntExtra(ARG_CRIME_ID, 0)
        Handler().postDelayed({
            id_viewpager.setCurrentItem(i, false)
        }, 100)
    }

    class PagerAdapter(activity: AppCompatActivity, private val itemsSize: Int) :
        FragmentStateAdapter(activity) {

        override fun getItemCount(): Int {
            return itemsSize
        }

        override fun createFragment(position: Int): Fragment {
            return CrimeFragment.newInstance(position)
        }
    }

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
    }
}