package com.adolfodev.countries.network

import com.adolfodev.countries.model.Country
import io.reactivex.Single
import retrofit2.http.GET

interface CountriesApi {

    @GET("/Devtides/countries/master/countriesV2.json")
    fun getCountries():Single<List<Country>>


}