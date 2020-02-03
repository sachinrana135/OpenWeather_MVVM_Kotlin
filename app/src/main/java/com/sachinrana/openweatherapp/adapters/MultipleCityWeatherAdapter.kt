package com.sachinrana.openweatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hellohasan.weatherappmvvm.features.weather_info_show.model.data_class.WeatherData
import com.sachinrana.openweatherapp.R
import kotlinx.android.synthetic.main.rv_multiple_city_weather_row.view.*

class MultipleCityWeatherAdapter(
    private val context: Context,
    private val weatherList: ArrayList<WeatherData>
) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)

        return ViewHolder(
            layoutInflater.inflate(
                R.layout.rv_multiple_city_weather_row,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.tv_city_name?.text = weatherList[position].cityName
        holder?.tv_weather_desc?.text = weatherList[position].weatherConditionIconDescription
        Glide.with(context).load(weatherList[position].weatherConditionIconUrl)
            .into(holder?.iv_weather)
        holder?.tv_max_temp?.text = weatherList[position].tempMax
        holder?.tv_min_temp?.text = weatherList[position].tempMin
        holder?.tv_wind_speed?.text = weatherList[position].windSpeed
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val tv_city_name = view.tv_city_name
    val tv_weather_desc = view.tv_weather_desc
    val iv_weather = view.iv_weather_condition
    val tv_max_temp = view.tv_temp_max
    val tv_min_temp = view.tv_temp_min
    val tv_wind_speed = view.tv_wind_speed
}