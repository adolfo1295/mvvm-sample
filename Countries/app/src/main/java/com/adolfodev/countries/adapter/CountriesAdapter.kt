package com.adolfodev.countries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adolfodev.countries.R
import com.adolfodev.countries.model.Country
import com.adolfodev.countries.util.getProgressDrawable
import com.adolfodev.countries.util.loadImage
import kotlinx.android.synthetic.main.item_country.view.*

class CountriesAdapter(var countries: ArrayList<Country>) :
    RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvCountryName: TextView = view.tvName
        private val tvCapitalName: TextView = view.tvCapitalName
        private val ivFlag: ImageView = view.ivFlag
        private val progressDrawable = getProgressDrawable(view.context)

        fun bindViews(country: Country) {
            tvCountryName.text = country.countryName
            tvCapitalName.text = country.capital
            ivFlag.loadImage(country.flag, progressDrawable)
        }
    }

    fun updateCountries(newCountries: List<Country>) {
        countries.clear()
        countries.addAll(newCountries)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_country, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bindViews(countries[position])
    }

}