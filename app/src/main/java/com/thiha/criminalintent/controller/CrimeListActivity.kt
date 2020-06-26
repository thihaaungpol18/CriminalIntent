package com.thiha.criminalintent.controller

import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.thiha.criminalintent.R
import com.thiha.criminalintent.model.Crime
import com.thiha.criminalintent.model.CrimeLab
import kotlinx.android.synthetic.main.fragment_list_crime.*
import java.util.*


class CrimeListActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return CrimeListFragment()
    }

    private fun updateSubtitle() {
        val subtitle = getQuantityString()
        if (mSubtitleVisible == false) {
            mSubtitleVisible = true
            supportActionBar?.subtitle = subtitle
        } else {
            mSubtitleVisible = false
            supportActionBar?.subtitle = ""
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_new_crime -> {
                CrimeLab.addCrime(
                    Crime(
                        0,
                        "Justin's Case",
                        Date(),
                        mSolved = false,
                        mRequiresPolice = true
                    )
                )
                id_recyclerView?.adapter?.notifyItemChanged(0)
                Snackbar.make(id_recyclerView, "Add", Snackbar.LENGTH_LONG).show()
            }
            R.id.menu_show_sub -> {
                this.invalidateOptionsMenu()
                updateSubtitle()
            }
        }
        return super.onOptionsItemSelected(item)
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

    private fun getQuantityString(): String {
        val crimeSize: Int = CrimeLab.getCrimes().size
        return resources.getQuantityString(R.plurals.subtitle_plural, crimeSize, crimeSize)
    }

    companion object {
        var mSubtitleVisible: Boolean? = null
    }

}