package com.thiha.criminalintent.ui

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.thiha.criminalintent.R
import kotlinx.android.synthetic.main.activity_crime_pager.*
import kotlinx.android.synthetic.main.fragment_crime.*


class CrimePagerActivity : AppCompatActivity() {

    private var pagerCallBack = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            Toast.makeText(this@CrimePagerActivity, "Position : $position", Toast.LENGTH_SHORT)
                .show()

            if (position == 0 && btn_first.visibility == Button.VISIBLE) {
                btn_first.visibility = Button.GONE
            } else {
                btn_first.visibility = Button.VISIBLE
            }
            if (position == (CrimeLab.getCrimes().size - 1) && btn_end.visibility == Button.VISIBLE) {
                btn_end.visibility = Button.GONE
            } else {
                btn_end.visibility = Button.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)
        val mCrimes = CrimeLab.get(this).getCrimes()
        val adapter = PagerAdapter(this@CrimePagerActivity, mCrimes.size)
        id_viewpager.adapter = adapter
        id_viewpager.registerOnPageChangeCallback(pagerCallBack)
        val i = intent.getIntExtra(ARG_CRIME_ID, 0)
        Handler().postDelayed({
            id_viewpager.setCurrentItem(i, false)
        }, 100)

        btn_first.setOnClickListener {
            Handler().postDelayed({
                id_viewpager.setCurrentItem(0, false)
            }, 100)
        }
        btn_end.setOnClickListener {
            Handler().postDelayed({
                id_viewpager.setCurrentItem(CrimeLab.getCrimes().size, false)
            }, 100)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crime, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            CrimeLab.deleteCrime(CrimeFragment.currentCrime).also {
                Snackbar.make(btn_crime_date, "Deleted", Snackbar.LENGTH_LONG).show()
            }
            finish()
        }
        return super.onOptionsItemSelected(item)
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