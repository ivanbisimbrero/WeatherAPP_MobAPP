package com.example.weatherapp_mobapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.SearchView
import com.example.weatherapp_mobapp.databinding.ActivityA3FavouriteCitiesListBinding

class A3FavouriteCitiesList : AppCompatActivity() {

    private val view by lazy { ActivityA3FavouriteCitiesListBinding.inflate(layoutInflater) }
    private var displayType = CityAdapter.TEMPERATURE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        val citiesUtils = SearchUtils(DataUtils.mainUser.cities)
        updateCityAdapter(citiesUtils.getAllCities())
        view.svSearchCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                updateCityAdapter(citiesUtils.searchCity(newText))
                return false
            }
        })
        view.rbgFilters.setOnCheckedChangeListener { _ , checkedId ->
            when(checkedId) {
                R.id.rbTemperature -> {
                    displayType = CityAdapter.TEMPERATURE
                }
                R.id.rbRainProb -> {
                    displayType = CityAdapter.RAIN_PROB
                }
                R.id.rbCondition -> {
                    displayType = CityAdapter.CONDITION
                }
            }
            updateCityAdapter(citiesUtils.getAllCities())
        }
    }

    fun updateCityAdapter(cities: List<City>) {
        val listView = findViewById<ListView>(R.id.lvCities)
        val adapter = CityAdapter(this, cities, displayType)
        listView.adapter = adapter
    }


}