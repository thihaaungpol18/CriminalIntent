package com.thiha.criminalintent.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thiha.criminalintent.R
import com.thiha.criminalintent.model.Crime
import java.text.SimpleDateFormat
import java.util.*

/**
project: CriminalIntent
Created by : Thiha
date : 6/19/2020
 */
class CrimeAdapter(
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var crimeLists = emptyList<Crime>()

    class SmallCrimeVH(itemView: View, private val onClickListener: OnClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onClickListener.onClick(adapterPosition)
        }
    }

    class BigCrimeVH(itemView: View, private val onClickListener: OnClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onClickListener.onClick(adapterPosition)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (crimeLists[position].requiredPolice) {
            false -> SmallCrime
            true -> BigCrime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SmallCrime) {
            SmallCrimeVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_small_crime, parent, false),
                onClickListener
            )
        } else {
            BigCrimeVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_big_crime, parent, false),
                onClickListener
            )
        }
    }

    override fun getItemCount(): Int = crimeLists.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentCrime = crimeLists[position]
        val tvTitle: TextView
        val tvDate: TextView
        if (holder.itemViewType == BigCrime) {
            tvTitle = holder.itemView.findViewById(R.id.item_title_big)
            tvDate = holder.itemView.findViewById(R.id.item_date_big)
        } else {
            tvTitle = holder.itemView.findViewById(R.id.item_title)
            tvDate = holder.itemView.findViewById(R.id.item_date)
        }
        tvTitle.text = currentCrime.title
        val formatter = SimpleDateFormat("EEE, d MMM yyyy hh:mm aaa", Locale.US)
        val date = formatter.format(currentCrime.date)
        tvDate.text = date
    }

    fun setCrimes(list: List<Crime>) {
        this.crimeLists = list
        notifyDataSetChanged()
    }

    companion object {
        const val BigCrime = 1
        const val SmallCrime = 0
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }
}