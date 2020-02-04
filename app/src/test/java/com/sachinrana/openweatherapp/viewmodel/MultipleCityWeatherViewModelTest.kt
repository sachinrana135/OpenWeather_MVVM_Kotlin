package com.sachinrana.openweatherapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hellohasan.weatherappmvvm.features.weather_info_show.model.data_class.City
import com.nhaarman.mockitokotlin2.capture
import com.sachinrana.openweatherapp.common.RequestCompleteListener
import com.sachinrana.openweatherapp.model.MultipleCityWeather
import com.sachinrana.openweatherapp.network.GetWeatherRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*

class MultipleCityWeatherViewModelTest {

    lateinit var multipleCityWeatherViewModel: MultipleCityWeatherViewModel

    @Mock
    lateinit var mockGetWeatherRepository: GetWeatherRepository

    @Mock
    lateinit var mockApplication: Application

    @Captor
    lateinit var captor: ArgumentCaptor<RequestCompleteListener<MultipleCityWeather>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`<Context>(mockApplication.applicationContext).thenReturn(mockApplication)
        multipleCityWeatherViewModel = MultipleCityWeatherViewModel()
    }

    @Test
    fun testGetWeatherInfoLiveData() {

        var cityIds = "1270642,1264352"
        val multipleCityWeather = MultipleCityWeather(3, listOf(), City())
        multipleCityWeatherViewModel.getWeatherInfo(cityIds, mockGetWeatherRepository)


        assertTrue(multipleCityWeatherViewModel.progressBarLiveData.value!!)
        Mockito.verify(mockGetWeatherRepository)
            .getMultipleCityWeatherInfo(cityIds, capture(captor))
        captor.value.onRequestSuccess(multipleCityWeather)

        assertFalse(multipleCityWeatherViewModel.progressBarLiveData.value!!)
        assertFalse(multipleCityWeatherViewModel.weatherInfoLiveData.value?.size == 0)

    }


}