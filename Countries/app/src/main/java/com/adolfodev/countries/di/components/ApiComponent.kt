package com.adolfodev.countries.di.components

import androidx.lifecycle.ViewModel
import com.adolfodev.countries.di.modules.ApiModule
import com.adolfodev.countries.network.CountriesService
import com.adolfodev.countries.viewmodel.CountriesViewModel
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [ApiModule::class]
)
@Singleton
interface ApiComponent {
    fun inject(service: CountriesService)
    fun inject(viewModel: CountriesViewModel)
}