package com.qwerty.ksoaptest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CitiesAdapter(private val mCitiesData: List<String>) : RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CitiesViewHolder(inflater.inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder.mCityNameTV.text = mCitiesData[position]
    }

    override fun getItemCount(): Int {
        return mCitiesData.size
    }

    inner class CitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mCityNameTV: TextView = itemView.findViewById(R.id.cities_textview)
    }
}