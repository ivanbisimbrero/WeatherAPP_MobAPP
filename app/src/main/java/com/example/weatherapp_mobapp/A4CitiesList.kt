package com.example.weatherapp_mobapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Spinner
import com.example.weatherapp_mobapp.databinding.ActivityA4CitiesListBinding

class A4CitiesList : AppCompatActivity() {

    private val view by lazy { ActivityA4CitiesListBinding.inflate(layoutInflater) }
    val citiesUtils = SearchUtils(DataUtils.mainUser.cities)
    private var displayType = CityAdapter.TEMPERATURE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        updateCityAdapter(citiesUtils.getAllCities())
        view.llCitiesList.lvCities.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, A5CityDetail::class.java).apply {
                // We give the selected city
                putExtra("cityPosition", position)
                //And finally with another boolean we send if it becomes from the Fav List Activity or not
                putExtra("favListActivity", false)
            }
            startActivity(intent)
        }
        view.llCitiesList.svSearchCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                updateCityAdapter(citiesUtils.searchCity(newText))
                return false
            }
        })
        view.llCitiesList.rbgFilters.setOnCheckedChangeListener { _ , checkedId ->
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

    override fun onResume() {
        super.onResume()
        updateCityAdapter(citiesUtils.getAllCities())
    }

    private fun updateCityAdapter(cities: List<City>) {
        val listView = findViewById<ListView>(R.id.lvCities)
        val adapter = CityAdapter(this, cities, displayType)
        listView.adapter = adapter
    }

}