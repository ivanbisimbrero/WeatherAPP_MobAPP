package com.example.weatherapp_mobapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.SearchView
import com.example.weatherapp_mobapp.databinding.ActivityA3FavouriteCitiesListBinding

class A3FavouriteCitiesList : AppCompatActivity() {

    private val view by lazy { ActivityA3FavouriteCitiesListBinding.inflate(layoutInflater) }
    private var displayType = CityAdapter.TEMPERATURE
    var favCitiesUtils = SearchUtils(DataUtils.mainUser.favCities)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        updateCityAdapter(favCitiesUtils.getAllCities())
        view.llFavCities.lvCities.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, A5CityDetail::class.java).apply {
                // We give the selected city
                putExtra("cityPosition", position)
                //And finally with another boolean we send if it becomes from the Fav List Activity or not
                putExtra("favListActivity", true)
            }
            startActivity(intent)
        }
        view.llFavCities.svSearchCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                updateCityAdapter(favCitiesUtils.searchCity(newText))
                return false
            }
        })
        view.llFavCities.rbgFilters.setOnCheckedChangeListener { _ , checkedId ->
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
            updateCityAdapter(favCitiesUtils.getAllCities())
        }
    }

    override fun onResume() {
        super.onResume()
        favCitiesUtils = SearchUtils(DataUtils.mainUser.favCities)
        updateCityAdapter(favCitiesUtils.getAllCities())
    }

    private fun updateCityAdapter(cities: List<City>) {
        val listView = findViewById<ListView>(R.id.lvCities)
        val adapter = CityAdapter(this, cities, displayType)
        listView.adapter = adapter
    }
}