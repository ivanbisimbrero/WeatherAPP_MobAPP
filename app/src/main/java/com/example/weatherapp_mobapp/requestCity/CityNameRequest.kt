package com.example.weatherapp_mobapp.requestCity

import com.example.weatherapp_mobapp.model.DefaultCities
import com.example.weatherapp_mobapp.utils.DataUtils

class CityNameRequest (
    val defaultCity : DefaultCities
) : WeatherRequest {
    override fun getURL() : String {
        return "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/${defaultCity.name},${defaultCity.region}?unitGroup=metric&key=${DataUtils.API_KEY}"
    }

    override suspend fun getName(): String {
        return defaultCity.name
    }
}