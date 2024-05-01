package com.example.weatherapp_mobapp

class SearchUtils(private val cities: MutableList<City>) {

    fun searchCity(searchName: String): List<City> {
        if (searchName.isEmpty()) {
            return cities
        }
        return cities.filter { it.name.contains(searchName, ignoreCase = true) }
    }

    fun getAllCities(): List<City> {
        return cities
    }

}