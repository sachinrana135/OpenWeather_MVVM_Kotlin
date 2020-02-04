package com.sachinrana.openweatherapp.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hellohasan.weatherappmvvm.features.weather_info_show.model.data_class.City
import java.io.BufferedReader
import java.lang.reflect.Type


open class CityRepository(private val context: Context) {

    fun getCityList(): MutableList<City> {
        val listType: Type = object : TypeToken<MutableList<City>?>() {}.type
        val bufferedReader: BufferedReader =
            context.assets.open("indian_cities.json").bufferedReader()
        val inputString = bufferedReader.use { it.readText() }
        return Gson().fromJson(inputString, listType)
    }
}