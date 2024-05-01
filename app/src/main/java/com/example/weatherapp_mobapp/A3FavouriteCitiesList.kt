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
    var favCitiesUtils = SearchUtils(getFavCities())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        updateCityAdapter(favCitiesUtils.getAllCities())
        view.lvFavCities.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, A5CityDetail::class.java).apply {
                // We give the selected city
                putExtra("cityPosition", position)
                //And finally with another boolean we send if it becomes from the Fav List Activity or not
                putExtra("favListActivity", true)
            }
            startActivity(intent)
        }
        view.svSearchFavCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                updateCityAdapter(favCitiesUtils.searchCity(newText))
                return false
            }
        })
        view.rbgFavFilters.setOnCheckedChangeListener { _ , checkedId ->
            when(checkedId) {
                R.id.rbFavTemperature -> {
                    displayType = CityAdapter.TEMPERATURE
                }
                R.id.rbFavRainProb -> {
                    displayType = CityAdapter.RAIN_PROB
                }
                R.id.rbFavCondition -> {
                    displayType = CityAdapter.CONDITION
                }
            }
            updateCityAdapter(favCitiesUtils.getAllCities())
        }
    }

    override fun onResume() {
        super.onResume()
        favCitiesUtils = SearchUtils(getFavCities())
        updateCityAdapter(favCitiesUtils.getAllCities())
    }

    private fun updateCityAdapter(cities: List<City>) {
        val listView = findViewById<ListView>(R.id.lvFavCities)
        val adapter = CityAdapter(this, cities, displayType)
        listView.adapter = adapter
    }

    private fun getFavCities() : MutableList<City> {
        val allCities = DataUtils.mainUser.cities
        val favCities = mutableListOf<City>()
        allCities.forEach {
            if(it.isFavouriteCity) {
                println(it.name + "is favourite, adding")
                favCities.add(it)
            }
        }
        return favCities
    }


}