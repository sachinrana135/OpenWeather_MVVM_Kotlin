package com.sachinrana.openweatherapp.model

import com.google.gson.annotations.SerializedName
import com.hellohasan.weatherappmvvm.features.weather_info_show.model.data_class.City
import com.hellohasan.weatherappmvvm.features.weather_info_show.model.data_class.WeatherInfoResponse

data class MultipleCityWeather(
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("list")
    val weatherList: List<WeatherInfoResponse> = listOf(),
    @SerializedName("city")
    val city: City
)