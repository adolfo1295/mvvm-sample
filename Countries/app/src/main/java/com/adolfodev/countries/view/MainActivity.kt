package com.adolfodev.countries.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.adolfodev.countries.R
import com.adolfodev.countries.adapter.CountriesAdapter
import com.adolfodev.countries.network.CountriesApi
import com.adolfodev.countries.viewmodel.CountriesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: CountriesViewModel
    private val countriesAdpater =
        CountriesAdapter(arrayListOf()) // passing an empty list


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(CountriesViewModel::class.java)
        viewModel.refresh()


        rvCountries.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdpater
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            viewModel.refresh()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.countries.observe(this, Observer { countries ->
            countries?.let {
                rvCountries.visibility = View.VISIBLE
                countriesAdpater.updateCountries(it)
            }
        })

        viewModel.countryLoadError.observe(this, Observer { isError ->
            isError?.let { tvError.visibility = if (it) View.VISIBLE else View.GONE }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    tvError.visibility = View.GONE
                    rvCountries.visibility = View.GONE
                }
            }
        })
    }
}
