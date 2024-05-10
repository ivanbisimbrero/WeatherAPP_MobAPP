package com.example.weatherapp_mobapp.utils

import com.example.weatherapp_mobapp.databinding.GeneralNextDaysBinding
import com.example.weatherapp_mobapp.databinding.GeneralTimeDetailTableBinding
import com.example.weatherapp_mobapp.databinding.MoreDetailsTableBinding
import com.example.weatherapp_mobapp.model.City

class WeatherUtils {
    companion object {
        fun setTableValues(tableBinding: GeneralTimeDetailTableBinding, city: City) {
            tableBinding.tvCityName.text = city.name
            val currentDay = city.days[0]
            tableBinding.tvCurrentTemperature.text = currentDay.temp.toString() + "º"
            tableBinding.tvWeatherCondition.text = currentDay.conditions
            tableBinding.tvMinValue.text = currentDay.tempmin.toString() + "º"
            tableBinding.tvMaxValue.text = currentDay.tempmax.toString() + "º"
        }

        fun setNextDaysValues(nextDaysBinding: GeneralNextDaysBinding, city: City) {
            val days = listOf(nextDaysBinding.tvDayPlusOne, nextDaysBinding.tvDayPlusTwo,
                nextDaysBinding.tvDayPlusThree, nextDaysBinding.tvDayPlusFour)
            val minTemperatures = listOf(nextDaysBinding.tvDayPlusOneMinValue, nextDaysBinding.tvDayPlusTwoMinValue,
                nextDaysBinding.tvDayPlusThreeMinValue, nextDaysBinding.tvDayPlusFourMinValue)
            val maxTemperatures = listOf(nextDaysBinding.tvDayPlusOneMaxValue, nextDaysBinding.tvDayPlusTwoMaxValue,
                nextDaysBinding.tvDayPlusThreeMaxValue, nextDaysBinding.tvDayPlusFourMaxValue)

            for (i in 1..4) {
                val auxDay = city.days[i]
                days[i-1].text = auxDay.datetime
                minTemperatures[i-1].text = auxDay.tempmin.toString() + "º"
                maxTemperatures[i-1].text = auxDay.tempmax.toString() + "º"
            }
        }

        fun setMoreDetailsValues(moreDetailsTableBinding: MoreDetailsTableBinding, city: City) {
            val currentDay = city.days[0]
            moreDetailsTableBinding.tvFeelLikeValue.text = currentDay.feelslike.toString() + "º"
            moreDetailsTableBinding.tvHumidityValue.text = currentDay.humidity.toString() + "%"
            moreDetailsTableBinding.tvRainProbValue.text = currentDay.precipprob.toString() + "%"
            moreDetailsTableBinding.tvWindSpeedValue.text = currentDay.windspeed.toString() + " km/h"
        }
    }

}