package com.example.weatherapp_mobapp

class CitiesListUtils(private val cities: List<City>) {

    fun searchCity(searchName: String): List<City> {
        if(searchName.isEmpty()) {
            return cities
        }
        return cities.filter { it.name.contains(searchName, ignoreCase = true) }
    }

    fun getCities(): List<City> {
        return cities
    }

}