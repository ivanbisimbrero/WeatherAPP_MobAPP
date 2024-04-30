package com.example.weatherapp_mobapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp_mobapp.databinding.ActivityA3FavouriteCitiesListBinding

class A3FavouriteCitiesList : AppCompatActivity() {

    private val view by lazy { ActivityA3FavouriteCitiesListBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        val citiesUtils = CitiesListUtils(DataUtils.mainUser.cities)

    }
}