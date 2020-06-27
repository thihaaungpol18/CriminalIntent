package com.thiha.criminalintent.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.thiha.criminalintent.R
import com.thiha.criminalintent.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_crime_pager.*

class PagerFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private lateinit var adapter: CrimePagerAdapter
    private var currentPosition: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (arguments != null) {
            currentPosition = requireArguments().getInt("clickPosition")

            Handler().postDelayed({
                id_viewpager.setCurrentItem(currentPosition!!, false)
            }, 100)

        }

        viewModel = ViewModelProvider(this@PagerFragment).get(ListViewModel::class.java)

        viewModel.allCrimes.observe(viewLifecycleOwner, Observer { list ->
            adapter = CrimePagerAdapter(this@PagerFragment, list.size)
            id_viewpager.adapter = adapter

            id_viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    if (position == 0 && btn_first.visibility == Button.VISIBLE) {
                        btn_first.visibility = Button.GONE
                    } else {
                        btn_first.visibility = Button.VISIBLE
                    }
                    if (position == (list.size - 1) && btn_end.visibility == Button.VISIBLE) {
                        btn_end.visibility = Button.GONE
                    } else {
                        btn_end.visibility = Button.VISIBLE
                    }
                }
            })

            btn_first.setOnClickListener {
                Handler().postDelayed({
                    id_viewpager.setCurrentItem(0, false)
                }, 100)
            }
            btn_end.setOnClickListener {
                Handler().postDelayed({
                    id_viewpager.setCurrentItem(list.size, false)
                }, 100)
            }

        })
    }


    class CrimePagerAdapter(fragment: Fragment, private val itemsSize: Int) :
        FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int {
            return itemsSize
        }

        override fun createFragment(position: Int): Fragment {
            return CrimeFragment.newInstance(position)
        }
    }

}