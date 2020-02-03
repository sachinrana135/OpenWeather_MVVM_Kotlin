package com.sachinrana.openweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hellohasan.weatherappmvvm.features.weather_info_show.model.data_class.WeatherData
import com.sachinrana.openweatherapp.common.RequestCompleteListener
import com.sachinrana.openweatherapp.model.MultipleCityWeather
import com.sachinrana.openweatherapp.network.GetWeatherRepository
import com.sachinrana.openweatherapp.utils.kelvinToCelsius
import com.sachinrana.openweatherapp.utils.unixTimestampToDateTimeString
import com.sachinrana.openweatherapp.utils.unixTimestampToTimeString

class MultipleCityWeatherViewModel : ViewModel() {

    val weatherInfoLiveData = MutableLiveData<ArrayList<WeatherData>>()
    val weatherInfoFailureLiveData = MutableLiveData<String>()
    val progressBarLiveData = MutableLiveData<Boolean>()

    fun getWeatherInfo(cityId: String, weatherRepository: GetWeatherRepository) {

        progressBarLiveData.postValue(true) // PUSH data to LiveData object to show progress bar

        weatherRepository.getMultipleCityWeatherInfo(cityId, object :
            RequestCompleteListener<MultipleCityWeather> {
            override fun onRequestSuccess(multipleCityWeather: MultipleCityWeather) {

                var weatherDataList = ArrayList<WeatherData>()
                val dataList = multipleCityWeather?.weatherList

                for (data in dataList) {
                    var weatherData = WeatherData(
                        dateTime = data.dt.unixTimestampToDateTimeString(),
                        temperature = data.main.temp.kelvinToCelsius().toString(),
                        tempMax = data.main.tempMax.kelvinToCelsius().toString(),
                        tempMin = data.main.tempMin.kelvinToCelsius().toString(),
                        cityAndCountry = "${data.name}, ${data.sys.country}",
                        weatherConditionIconUrl = "http://openweathermap.org/img/w/${data.weather[0].icon}.png",
                        weatherConditionIconDescription = data.weather[0].description,
                        humidity = "${data.main.humidity}%",
                        pressure = "${data.main.pressure} mBar",
                        visibility = "${data.visibility / 1000.0} KM",
                        sunrise = data.sys.sunrise.unixTimestampToTimeString(),
                        sunset = data.sys.sunset.unixTimestampToTimeString(),
                        windSpeed = data.wind.speed.toString(),
                        cityName = data.name
                    )
                    weatherDataList.add(weatherData)
                }

                progressBarLiveData.postValue(false) // PUSH data to LiveData object to hide progress bar
                weatherInfoLiveData.postValue(weatherDataList) // PUSH data to LiveData object
            }

            override fun onRequestFailed(errorMessage: String) {
                progressBarLiveData.postValue(false) // hide progress bar
                weatherInfoFailureLiveData.postValue(errorMessage) // PUSH error message to LiveData object
            }
        })
    }

}