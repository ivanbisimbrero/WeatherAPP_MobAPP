package com.example.weatherapp_mobapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.weatherapp_mobapp.databinding.ActivityMainBinding

class A2MainActivity : AppCompatActivity() {

    val NEXT_DAYS_CHECK = 4

    private val view by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        setValues()
        setButtonListeners()
    }

    private fun setValues() {
        view.tvCityName.text = DataUtils.mainUser.currentCity.name
        var currentDay = DataUtils.mainUser.currentCity.days[0]
        view.tvCurrentTemperature.text = currentDay.temp.toString() + "º"
        view.tvWeatherCondition.text = currentDay.conditions
        view.tvMinValue.text = currentDay.tempmin.toString() + "º"
        view.tvMaxValue.text = currentDay.tempmax.toString() + "º"
        setNextDaysValues()
    }

    private fun setNextDaysValues() {

        var days = mutableListOf(view.tvDayPlusOne, view.tvDayPlusTwo, view.tvDayPlusThree,
            view.tvDayPlusFour)
        var minTemperatures = mutableListOf(view.tvDayPlusOneMinValue, view.tvDayPlusTwoMinValue,
            view.tvDayPlusThreeMinValue, view.tvDayPlusFourMinValue)
        var maxTemperatures = mutableListOf(view.tvDayPlusOneMaxValue, view.tvDayPlusTwoMaxValue,
            view.tvDayPlusThreeMaxValue, view.tvDayPlusFourMaxValue)

        for (i in 1..NEXT_DAYS_CHECK) {
            var auxDay = DataUtils.mainUser.currentCity.days[i]
            days[i-1].text = auxDay.datetime
            minTemperatures[i-1].text = auxDay.tempmin.toString() + "º"
            maxTemperatures[i-1].text = auxDay.tempmax.toString() + "º"
        }
    }

    private fun setButtonListeners() {
        view.btnCititesList.setOnClickListener {
            val intent = Intent(this, A4CitiesList::class.java)
            startActivity(intent)
        }
        view.btnFavCitites.setOnClickListener {
            val intent = Intent(this, A3FavouriteCitiesList::class.java)
            startActivity(intent)
        }
    }
}