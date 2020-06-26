package com.thiha.criminalintent.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thiha.criminalintent.R

/**
project: CriminalIntent
Created by : Thiha
date : 6/19/2020
 */
class CrimeAdapter(
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class SmallCrimeVH(itemView: View, private val onClickListener: OnClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            tvTitle = itemView.findViewById(R.id.item_title)
            tvDate = itemView.findViewById(R.id.item_date)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onClickListener.onClick(adapterPosition)
        }
    }

    class BigCrimeVH(itemView: View, private val onClickListener: OnClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            tvTitle = itemView.findViewById(R.id.item_title)
            tvDate = itemView.findViewById(R.id.item_date)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onClickListener.onClick(adapterPosition)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (CrimeLab.getCrime(position).mRequiresPolice) {
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

    override fun getItemCount(): Int = CrimeLab.getCrimes().size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentCrime = CrimeLab.getCrime(position)
        tvTitle.text = currentCrime.mTitle
        tvDate.text = DateToString().formatDate(currentCrime.mDate)
    }

    companion object {
        private lateinit var tvTitle: TextView
        private lateinit var tvDate: TextView
        const val BigCrime = 1
        const val SmallCrime = 0
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }
}