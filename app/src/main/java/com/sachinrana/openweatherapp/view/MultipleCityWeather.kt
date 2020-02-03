package com.sachinrana.openweatherapp.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sachinrana.openweatherapp.R
import com.sachinrana.openweatherapp.adapters.MultipleCityWeatherAdapter
import com.sachinrana.openweatherapp.network.GetWeatherRepository
import com.sachinrana.openweatherapp.utils.Constant
import com.sachinrana.openweatherapp.viewmodel.MultipleCityWeatherViewModel
import kotlinx.android.synthetic.main.activity_mutliple_city_weather.*

class MultipleCityWeather : AppCompatActivity() {

    private lateinit var weatherRepository: GetWeatherRepository
    private lateinit var viewModel: MultipleCityWeatherViewModel
    var selectedCity = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mutliple_city_weather)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_multiple_city_weather)

        selectedCity = intent?.getIntegerArrayListExtra(Constant.INTENT_SELECTED_CITIES)!!

        weatherRepository = GetWeatherRepository(applicationContext)
        // initialize ViewModel
        viewModel = ViewModelProviders.of(this).get(MultipleCityWeatherViewModel::class.java)

        setLiveDataListeners()

        viewModel.getWeatherInfo(
            android.text.TextUtils.join(",", selectedCity),
            weatherRepository
        ) // fetch weather info

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setLiveDataListeners() {

        viewModel.progressBarLiveData.observe(this, Observer { isShowLoader ->
            if (isShowLoader)
                progressBar.visibility = View.VISIBLE
            else
                progressBar.visibility = View.GONE
        })

        viewModel.weatherInfoLiveData.observe(this, Observer { weatherData ->
            val adapter = MultipleCityWeatherAdapter(this, weatherData)
            rv_multiple_city_weather.adapter = adapter
        })

    }
}
