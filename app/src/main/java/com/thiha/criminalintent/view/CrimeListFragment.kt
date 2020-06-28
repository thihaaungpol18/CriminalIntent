package com.thiha.criminalintent.view

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.thiha.criminalintent.R
import com.thiha.criminalintent.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list_crime.*


/**
project: CriminalIntent
Created by : Thiha
date : 6/18/2020
 */
class CrimeListFragment : Fragment(), CrimeAdapter.OnClickListener {
    private lateinit var adapter: CrimeAdapter
    private lateinit var viewModel: ListViewModel
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_list_crime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        toolbar = view.findViewById(R.id.id_toolbar_list)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        viewModel =
            ViewModelProvider(this@CrimeListFragment).get(ListViewModel::class.java)

        adapter = CrimeAdapter(this)

        id_recyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        id_recyclerView.adapter = adapter

        viewModel.allCrimes.observe(viewLifecycleOwner, Observer { list ->
            adapter.setCrimes(list)
            if (list.isEmpty()) {
                id_recyclerView.visibility = RecyclerView.GONE
                IVnoItem.visibility = ImageView.VISIBLE
                Glide
                    .with(requireContext())
                    .load(R.drawable.img_no_data)
                    .into(IVnoItem)
            } else {
                id_recyclerView.visibility = RecyclerView.VISIBLE
                IVnoItem.visibility = ImageView.GONE


                val itemTouchHelper = ItemTouchHelper(
                    object :
                        ItemTouchHelper.SimpleCallback(
                            0,
                            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                        ) {
                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: ViewHolder,
                            target: ViewHolder
                        ): Boolean {
                            return false
                        }

                        override fun onSwiped(
                            viewHolder: ViewHolder,
                            direction: Int
                        ) {
                            viewModel.delete(list[viewHolder.adapterPosition])
                            adapter.notifyItemRemoved(viewHolder.adapterPosition)
                        }
                    })

                itemTouchHelper.attachToRecyclerView(id_recyclerView)
            }

        })

        view.findViewById<FloatingActionButton>(R.id.id_fab).setOnClickListener {
            findNavController().navigate(R.id.toDetail)
        }

        //toolbar
        val appBarConfig = AppBarConfiguration(setOf(R.id.listFrag))
        val host = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, host, appBarConfig)
    }

    override fun onResume() {
        super.onResume()
        viewModel.allCrimes.observe(viewLifecycleOwner, Observer { list ->
            adapter.setCrimes(list)
            if (list.isEmpty()) {
                id_recyclerView.visibility = RecyclerView.GONE
                IVnoItem.visibility = ImageView.VISIBLE
                Glide
                    .with(requireContext())
                    .load(R.drawable.img_no_data)
                    .into(IVnoItem)


            } else {
                id_recyclerView.visibility = RecyclerView.VISIBLE
                IVnoItem.visibility = ImageView.GONE


                val itemTouchHelper = ItemTouchHelper(
                    object :
                        ItemTouchHelper.SimpleCallback(
                            0,
                            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                        ) {
                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: ViewHolder,
                            target: ViewHolder
                        ): Boolean {
                            return false
                        }

                        override fun onSwiped(
                            viewHolder: ViewHolder,
                            direction: Int
                        ) {
                            viewModel.delete(list[viewHolder.adapterPosition])
                            adapter.notifyItemRemoved(viewHolder.adapterPosition)
                        }
                    })

                itemTouchHelper.attachToRecyclerView(id_recyclerView)
            }
        })

    }

    override fun onClick(position: Int) {
        val bundle = bundleOf("clickPosition" to position)
        findNavController().navigate(R.id.toPagerFrag, bundle)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_show_sub) {
            if (toolbar.subtitle.isNullOrEmpty()) {
                showSubtitle()
                item.title = resources.getString(R.string.hide_subtitle)
            } else {
                item.title = resources.getString(R.string.show_subtitle)
                toolbar.subtitle = ""
            }
            return true
        }
        return false
    }

    private fun showSubtitle() {
        viewModel.allCrimes.observe(viewLifecycleOwner, Observer {
            toolbar.subtitle =
                resources.getQuantityString(R.plurals.subtitle_plural, it.size, it.size)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_crime_list, menu)
    }

}