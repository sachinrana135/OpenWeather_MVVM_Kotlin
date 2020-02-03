package com.sachinrana.openweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hellohasan.weatherappmvvm.features.weather_info_show.model.data_class.City
import com.sachinrana.openweatherapp.network.CityRepository

class CitySelectionViewModel : ViewModel() {

    val cityListLiveData = MutableLiveData<MutableList<City>>()

    fun getCityList(searchText: String, cityRepository: CityRepository) {

        if (searchText.length > 2) {
            var cityList = cityRepository.getCityList()
                .filter { it?.name.startsWith(searchText, true) }.toMutableList()

            cityListLiveData.postValue(cityList)
        } else {
            cityListLiveData.postValue(mutableListOf<City>())
        }

    }
}