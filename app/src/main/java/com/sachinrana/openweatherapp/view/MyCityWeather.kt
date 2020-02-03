package com.sachinrana.openweatherapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.*
import com.sachinrana.openweatherapp.R
import com.sachinrana.openweatherapp.adapters.MyCityWeatherAdapter
import com.sachinrana.openweatherapp.network.GetWeatherRepository
import com.sachinrana.openweatherapp.viewmodel.MyCityWeatherViewModel
import kotlinx.android.synthetic.main.activity_mutliple_city_weather.progressBar
import kotlinx.android.synthetic.main.activity_my_city_weather.*


class MyCityWeather : AppCompatActivity() {

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var weatherRepository: GetWeatherRepository
    private lateinit var viewModel: MyCityWeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_city_weather)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_weather_forecast)
        weatherRepository = GetWeatherRepository(applicationContext)
        viewModel = ViewModelProviders.of(this).get(MyCityWeatherViewModel::class.java)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setLiveDataListeners()
    }

    override fun onResume() {
        super.onResume()
        getLastLocation()
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

        viewModel.progressBarLiveData.observe(this,
            androidx.lifecycle.Observer<Boolean> { isShowLoader ->
                if (isShowLoader)
                    progressBar.visibility = View.VISIBLE
                else
                    progressBar.visibility = View.GONE
            })


        viewModel.weatherInfoLiveData.observe(this, androidx.lifecycle.Observer { weatherData ->
            tv_city_name.text = weatherData[0]?.cityName
            val adapter = MyCityWeatherAdapter(this, weatherData)
            rv_my_city_weather.adapter = adapter
        })

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        viewModel.getWeatherInfo(
                            location.latitude.toString(),
                            location.longitude.toString(),
                            weatherRepository
                        )

                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            viewModel.getWeatherInfo(
                mLastLocation.latitude.toString(),
                mLastLocation.longitude.toString(),
                weatherRepository
            )
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}
