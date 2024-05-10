package com.example.weatherapp_mobapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp_mobapp.adapter.CityAdapter
import com.example.weatherapp_mobapp.model.City
import com.example.weatherapp_mobapp.sharedPreferences.CrudAPI
import com.example.weatherapp_mobapp.sharedPreferences.SHARED_PREFERENCES_NAME
import com.example.weatherapp_mobapp.sharedPreferences.SharedPreferencesRepository
import com.example.weatherapp_mobapp.utils.SearchUtils

abstract class BaseCityListActivity : AppCompatActivity() {
    abstract val cityUtils: SearchUtils
    abstract val isFavouriteList: Boolean
    private lateinit var listView: ListView
    private var displayType = CityAdapter.TEMPERATURE
    private val repository: CrudAPI by lazy {
        SharedPreferencesRepository(
            application.getSharedPreferences(
                SHARED_PREFERENCES_NAME,
                MODE_PRIVATE
            )
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(provideLayout())

        listView = findViewById(provideListViewId())
        updateCityAdapter(cityUtils.getAllCities())

        findViewById<SearchView>(provideSearchViewId()).setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false
            override fun onQueryTextChange(newText: String): Boolean {
                updateCityAdapter(cityUtils.searchCity(newText))
                return false
            }
        })

        findViewById<RadioGroup>(provideRadioGroupId()).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
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
            updateCityAdapter(cityUtils.getAllCities())
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, A5CityDetail::class.java).apply {
                putExtra("cityPosition", position)
                putExtra("favListActivity", isFavouriteList)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateCityAdapter(cityUtils.getAllCities())
    }

    private fun updateCityAdapter(cities: List<City>) {
        val adapter = CityAdapter(this, cities, displayType, repository)
        listView.adapter = adapter
    }

    abstract fun provideLayout(): View
    abstract fun provideListViewId(): Int
    abstract fun provideSearchViewId(): Int
    abstract fun provideRadioGroupId(): Int
}