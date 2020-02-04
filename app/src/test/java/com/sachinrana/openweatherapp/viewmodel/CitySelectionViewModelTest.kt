package com.sachinrana.openweatherapp.viewmodel

import android.app.Application
import com.hellohasan.weatherappmvvm.features.weather_info_show.model.data_class.City
import com.sachinrana.openweatherapp.network.CityRepository
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class CitySelectionViewModelTest {

    lateinit var citySelectionViewModel: CitySelectionViewModel

    @Mock
    lateinit var mockApplication: Application

    lateinit var mockCityRepository: CityRepository


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mockCityRepository = mock(CityRepository(mockApplication)::class.java)
        citySelectionViewModel = CitySelectionViewModel()
    }

    @Test
    fun testGetCityList() {

        var searchText = "Gurgaon"
        var city = City(1, "Gurgaon", "India")
        var mockList = mutableListOf(city)


        `when`(mockCityRepository.getCityList()).thenReturn(mockList)

        citySelectionViewModel.getCityList(searchText, mockCityRepository)

        assertTrue(citySelectionViewModel.cityListLiveData.value?.size == 1)
        citySelectionViewModel.cityListLiveData.value?.contains(city)?.let { assertTrue(it) }


    }

}