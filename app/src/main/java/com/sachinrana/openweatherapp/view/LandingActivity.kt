package com.sachinrana.openweatherapp.presention.landing.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sachinrana.openweatherapp.R
import com.sachinrana.openweatherapp.view.CitySelection
import com.sachinrana.openweatherapp.view.MyCityWeather
import kotlinx.android.synthetic.main.activity_landing.*

class LandingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        setViewClickListener()
    }


    private fun setViewClickListener() {
        // View Weather button click listener
        btn_multiple_city.setOnClickListener {
            startActivity(Intent(this, CitySelection::class.java))
        }

        btn_single_city_forecast.setOnClickListener {
            startActivity(Intent(this, MyCityWeather::class.java))
        }
    }


}
