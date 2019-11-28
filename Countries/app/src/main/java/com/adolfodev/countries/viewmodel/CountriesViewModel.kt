package com.adolfodev.countries.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adolfodev.countries.di.components.DaggerApiComponent
import com.adolfodev.countries.model.Country
import com.adolfodev.countries.network.CountriesService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CountriesViewModel : ViewModel() {

    @Inject
    lateinit var countriesService: CountriesService

    init {
        DaggerApiComponent.create().inject(this)
    }

    private val dispossable = CompositeDisposable()

    val countries = MutableLiveData<List<Country>>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchCountries()
    }

    private fun fetchCountries() {
        loading.value = true
        dispossable.add(
            countriesService.getCountries()
                .subscribeOn(Schedulers.newThread()) //gonnaa be on a different thread
                .observeOn(AndroidSchedulers.mainThread()) //result gonna be on main thread
                .subscribeWith(object : DisposableSingleObserver<List<Country>>() {
                    override fun onSuccess(countriesResult: List<Country>) {
                        countries.value = countriesResult
                        countryLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(error: Throwable) {
                        countryLoadError.value = true
                        loading.value = false
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        dispossable.clear()
    }

}