package com.example.weatherapp_mobapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp_mobapp.databinding.ActivityA4CitiesListBinding

class A4CitiesList : AppCompatActivity() {

    private val view by lazy { ActivityA4CitiesListBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        val citiesUtils = SearchUtils(DataUtils.mainUser.cities)
    }
}