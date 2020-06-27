package com.thiha.criminalintent.ui

import android.view.Menu
import androidx.fragment.app.Fragment
import com.thiha.criminalintent.R


class CrimeListActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return CrimeListFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crime_list, menu)
        val subtitleItem = menu!!.findItem(R.id.menu_show_sub)
        mSubtitleVisible = false
        if (mSubtitleVisible == true) {
            subtitleItem.setTitle(R.string.hide_subtitle)
        } else {
            subtitleItem.setTitle(R.string.show_subtitle)
        }
        return super.onCreateOptionsMenu(menu)
    }

//    private fun getQuantityString(): String {
//        val crimeSize: Int = CrimeLab.getCrimes().size
//        return resources.getQuantityString(R.plurals.subtitle_plural, crimeSize, crimeSize)
//    }

    companion object {
        var mSubtitleVisible: Boolean? = null
    }

}