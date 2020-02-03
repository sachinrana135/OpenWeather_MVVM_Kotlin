package com.hellohasan.weatherappmvvm.network

import com.sachinrana.openweatherapp.model.MultipleCityWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("forecast")
    fun getWeatherForecastByLatLong(@Query("lat") lat: String, @Query("lon") lon: String): Call<MultipleCityWeather>

    @GET("group")
    fun getMultipleCityWeather(@Query("id") id: String): Call<MultipleCityWeather>

}