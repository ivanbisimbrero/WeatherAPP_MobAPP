package com.example.weatherapp_mobapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.example.weatherapp_mobapp.databinding.ActivityA1SplashScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class A1SplashScreen : AppCompatActivity() {


    private val view by lazy { ActivityA1SplashScreenBinding.inflate(layoutInflater) }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(view.root);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // If permissions not given, ask the user for it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }
        // If given permissions, we retrieve the data from the API
        val jobs = mutableListOf<Job>()
        val scope = CoroutineScope(Dispatchers.IO)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location ->
                println("Latitud: " + location.latitude)
                println("Longitud: " + location.longitude)

                val coordRequest: CityCoordinatesRequest = CityCoordinatesRequest(location.latitude, location.longitude)

                scope.launch {
                    val apiResponse = fetchWeather(coordRequest)
                    DataUtils.fillCurrentCity(coordRequest, apiResponse)
                }

            }

        //Then, we add the default cities jobs
        DataUtils.defaultRequests.forEach {defaultRequest ->
            val job = scope.launch {
                val apiResponse = fetchWeather(defaultRequest)
                DataUtils.fillCities(defaultRequest, apiResponse)
            }
            jobs.add(job)
        }

        //We move the wait logic to one coroutine
        scope.launch {
            jobs.joinAll()
            DataUtils.initUser()
            withContext(Dispatchers.Main) {
                startActivity(Intent(this@A1SplashScreen, A2MainActivity::class.java))
                finish()
            }
        }
    }

}