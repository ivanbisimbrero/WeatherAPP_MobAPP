package com.example.weatherapp_mobapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.weatherapp_mobapp.databinding.ActivityA5CityDetailBinding

class A5CityDetail : AppCompatActivity() {

    private val view by lazy { ActivityA5CityDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        val position = intent.getIntExtra("cityPosition", -1)
        val favListActivity = intent.getBooleanExtra("favListActivity", false)
        var city: City
        if(favListActivity) {
            city = DataUtils.mainUser.favCities[position]
        } else {
            city = DataUtils.mainUser.cities[position]
        }
        view.swchToggleFav.isChecked = city.isFavouriteCity
        view.swchToggleFav.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                city.isFavouriteCity = true
                DataUtils.mainUser.favCities.add(city)
            } else {
                city.isFavouriteCity = false
                DataUtils.mainUser.favCities.remove(city)
            }
        }
        setValues(city)
    }

    private fun setValues(city: City) {
        view.tvCityName.text = city.name
        var currentDay = city.days[0]
        view.tvCurrentTemperature.text = currentDay.temp.toString() + "º"
        view.tvWeatherCondition.text = currentDay.conditions
        view.tvMinValue.text = currentDay.tempmin.toString() + "º"
        view.tvMaxValue.text = currentDay.tempmax.toString() + "º"
    }
}