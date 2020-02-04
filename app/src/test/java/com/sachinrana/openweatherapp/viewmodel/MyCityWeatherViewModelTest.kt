package com.sachinrana.openweatherapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hellohasan.weatherappmvvm.features.weather_info_show.model.data_class.City
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.capture
import com.sachinrana.openweatherapp.common.RequestCompleteListener
import com.sachinrana.openweatherapp.model.MultipleCityWeather
import com.sachinrana.openweatherapp.network.GetWeatherRepository
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MyCityWeatherViewModelTest {

    lateinit var myCityWeatherViewModel: MyCityWeatherViewModel

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
        `when`<Context>(mockApplication.applicationContext).thenReturn(mockApplication)
        myCityWeatherViewModel = MyCityWeatherViewModel()
    }

    @Test
    fun testGetWeatherInfoLiveData() {

        var lat = "28.506892"
        var long = "77.0681237"
        val multipleCityWeather = MultipleCityWeather(3, listOf(), City())
        myCityWeatherViewModel.getWeatherInfo(lat, long, mockGetWeatherRepository)


        assertTrue(myCityWeatherViewModel.progressBarLiveData.value!!)
        verify(mockGetWeatherRepository).getWeatherByLatLong(any(), any(), capture(captor))
        captor.value.onRequestSuccess(multipleCityWeather)

        assertFalse(myCityWeatherViewModel.progressBarLiveData.value!!)
        assertFalse(myCityWeatherViewModel.weatherInfoLiveData.value?.size == 1)

    }

}