package com.thiha.criminalintent.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thiha.criminalintent.OnClickListener
import com.thiha.criminalintent.R

/**
project: CriminalIntent
Created by : Thiha
date : 6/19/2020
 */
class CrimeAdapter(
    private val mCrimeList: List<Crime>,
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<CrimeAdapter.CrimeVH>() {
    class CrimeVH(itemView: View, private val onClickListener: OnClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            tvTitle = itemView.findViewById(R.id.item_title)
            tvDate = itemView.findViewById(R.id.item_date)
            itemView.setOnClickListener(this)
        }

        fun bind(currentCrime: Crime) {
            tvTitle?.text = currentCrime.mTitle
            tvDate?.text = currentCrime.mDate.toString()
        }

        override fun onClick(v: View?) {
            onClickListener.onClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeVH = CrimeVH(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_crime, parent, false),
        onClickListener
    )

    override fun getItemCount(): Int = mCrimeList.size

    override fun onBindViewHolder(holder: CrimeVH, position: Int) {
        holder.bind(mCrimeList[position])
    }

    companion object {
        private var tvTitle: TextView? = null
        private var tvDate: TextView? = null
    }
}