package com.sachinrana.openweatherapp.network

import android.content.Context
import com.hellohasan.weatherappmvvm.network.ApiInterface
import com.hellohasan.weatherappmvvm.network.RetrofitClient
import com.sachinrana.openweatherapp.common.RequestCompleteListener
import com.sachinrana.openweatherapp.model.MultipleCityWeather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetWeatherRepository(private val context: Context) {

    fun getMultipleCityWeatherInfo(
        cityIds: String,
        callback: RequestCompleteListener<MultipleCityWeather>
    ) {

        val apiInterface: ApiInterface = RetrofitClient.client.create(ApiInterface::class.java)
        val call: Call<MultipleCityWeather> = apiInterface.getMultipleCityWeather(cityIds)

        call.enqueue(object : Callback<MultipleCityWeather> {

            // if retrofit network call success, this method will be triggered
            override fun onResponse(
                call: Call<MultipleCityWeather>,
                response: Response<MultipleCityWeather>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!) //let presenter know the weather information data
                else
                    callback.onRequestFailed(response.message()) //let presenter know about failure
            }

            // this method will be triggered if network call failed
            override fun onFailure(call: Call<MultipleCityWeather>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!) //let presenter know about failure
            }
        })
    }

    fun getWeatherByLatLong(
        lat: String,
        long: String,
        callback: RequestCompleteListener<MultipleCityWeather>
    ) {

        val apiInterface: ApiInterface = RetrofitClient.client.create(ApiInterface::class.java)
        val call: Call<MultipleCityWeather> = apiInterface.getWeatherForecastByLatLong(lat, long)

        call.enqueue(object : Callback<MultipleCityWeather> {

            // if retrofit network call success, this method will be triggered
            override fun onResponse(
                call: Call<MultipleCityWeather>,
                response: Response<MultipleCityWeather>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!) //let presenter know the weather information data
                else
                    callback.onRequestFailed(response.message()) //let presenter know about failure
            }

            // this method will be triggered if network call failed
            override fun onFailure(call: Call<MultipleCityWeather>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!) //let presenter know about failure
            }
        })
    }
}