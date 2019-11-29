package com.adolfodev.countries

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adolfodev.countries.model.Country
import com.adolfodev.countries.network.CountriesService
import com.adolfodev.countries.viewmodel.CountriesViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class CountriesViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var countriesService: CountriesService

    @InjectMocks
    var countriesViewModel = CountriesViewModel()

    private var testSingle: Single<List<Country>>? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getCountriesSucces() {
        val country = Country("Name", "Capital", "flag")
        val countries: ArrayList<Country> = arrayListOf(country)

        testSingle = Single.just(countries)

        `when`(countriesService.getCountries()).thenReturn(testSingle)
        countriesViewModel.refresh()

        Assert.assertEquals(1, countriesViewModel.countries.value?.size)
        Assert.assertEquals(false, countriesViewModel.countryLoadError.value)
        Assert.assertEquals(false, countriesViewModel.loading.value)
    }

    @Test
    fun getCountriesError() {
        testSingle = Single.error(Throwable())
        `when`(countriesService.getCountries()).thenReturn(testSingle)
        countriesViewModel.refresh()
        Assert.assertEquals(true, countriesViewModel.countryLoadError.value)
        Assert.assertEquals(false, countriesViewModel.loading.value)
    }

    @Before
    fun setUpRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
            }
        }
        RxJavaPlugins.setInitIoSchedulerHandler { scheduer -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }

}