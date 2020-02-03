package com.sachinrana.openweatherapp.view

import CityClickListener
import SearchAdapter
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hellohasan.weatherappmvvm.features.weather_info_show.model.data_class.City
import com.sachinrana.openweatherapp.R
import com.sachinrana.openweatherapp.network.CityRepository
import com.sachinrana.openweatherapp.utils.Constant
import com.sachinrana.openweatherapp.viewmodel.CitySelectionViewModel
import kotlinx.android.synthetic.main.activity_city_selection.*


class CitySelection : AppCompatActivity(), CityClickListener {

    private lateinit var searchView: SearchView
    private lateinit var viewModel: CitySelectionViewModel
    private lateinit var cityRepository: CityRepository
    private var cityIds = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_selection)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_select_city)
        viewModel = ViewModelProviders.of(this).get(CitySelectionViewModel::class.java)
        cityRepository = CityRepository(
            applicationContext
        )
        setLiveDataListeners()
        setClickListener()
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

    private fun setClickListener() {
        btn_clear_city.setOnClickListener {
            cityIds.clear()
            ll_selected_city.removeAllViews()
        }

        btn_submit_city.setOnClickListener {
            if (cityIds.size < 3) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.error_min_city_selection),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                var intent = Intent(this, MultipleCityWeather::class.java)
                intent.putIntegerArrayListExtra(Constant.INTENT_SELECTED_CITIES, cityIds)

                startActivity(intent)
            }
        }
    }

    private fun setLiveDataListeners() {
        viewModel.cityListLiveData.observe(this,
            Observer<MutableList<City>> { cities ->
                val adapter = SearchAdapter(
                    this@CitySelection,
                    R.layout.search_item,
                    cities,
                    this@CitySelection as CityClickListener
                )
                lvSearchResult.adapter = adapter
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_searchview, menu)
        val searchItem = menu!!.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.setSubmitButtonEnabled(true)
        searchView.setQueryHint(getString(R.string.text_enter_city))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.getCityList(newText, cityRepository)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getCityList(query, cityRepository)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCityClick(city: City) {
        if (cityIds.contains(city.id)) {
            Toast.makeText(
                applicationContext,
                getString(R.string.error_duplicate_city),
                Toast.LENGTH_SHORT
            ).show()
        } else if (cityIds.size < 7) {
            cityIds.add(city.id)
            searchView.setQuery("", false)
            searchView.clearFocus()
            val newCity = TextView(this).also {
                it.setText(city.name)
                it.gravity = Gravity.CENTER_HORIZONTAL
                it.setPadding(0, 10, 0, 0)
            }
            ll_selected_city.addView(newCity)
        } else {
            Toast.makeText(
                applicationContext,
                getString(R.string.error_max_city_selection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}
