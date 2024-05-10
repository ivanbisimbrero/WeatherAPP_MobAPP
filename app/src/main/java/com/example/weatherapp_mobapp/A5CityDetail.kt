package com.example.weatherapp_mobapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.weatherapp_mobapp.databinding.ActivityA5CityDetailBinding
import com.example.weatherapp_mobapp.sharedPreferences.CrudAPI
import com.example.weatherapp_mobapp.sharedPreferences.SHARED_PREFERENCES_NAME
import com.example.weatherapp_mobapp.sharedPreferences.SharedPreferencesRepository

class A5CityDetail : AppCompatActivity() {

    private val view by lazy { ActivityA5CityDetailBinding.inflate(layoutInflater) }
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
        setContentView(view.root)
        val position = intent.getIntExtra("cityPosition", -1)
        val favListActivity = intent.getBooleanExtra("favListActivity", false)
        val city: City = if(favListActivity) {
            DataUtils.mainUser.favCities[position]
        } else {
            DataUtils.mainUser.cities[position]
        }
        view.swchToggleFav.isChecked = city.isFavouriteCity
        view.swchToggleFav.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                city.isFavouriteCity = true
                DataUtils.mainUser.favCities.add(city)
                repository.save(city.name)
            } else {
                city.isFavouriteCity = false
                DataUtils.mainUser.favCities.remove(city)
                repository.delete(city.name)
            }
        }
        view.btnHistoricalData.setOnClickListener {
            val intent = Intent(this, A6HistoricalData::class.java).apply {
                putExtra("cityName", city.name)
            }
            startActivity(intent)
        }
        WeatherUtils.setTableValues(view.tDetailWeather, city)
        WeatherUtils.setNextDaysValues(view.llDetailNextDays, city)
        WeatherUtils.setMoreDetailsValues(view.llDetailMoreInfo, city)
    }
}