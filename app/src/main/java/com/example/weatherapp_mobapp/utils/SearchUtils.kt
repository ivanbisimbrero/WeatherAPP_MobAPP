package com.example.weatherapp_mobapp.utils

import com.example.weatherapp_mobapp.model.City

class SearchUtils(private val cities: MutableList<City>) {

    fun searchCity(searchName: String): List<City> {
        if (searchName.isEmpty()) {
            return cities
        }
        return cities.filter { it.name.contains(searchName, ignoreCase = true) }
    }

    fun getAllCities(): MutableList<City> {
        return cities
    }

}