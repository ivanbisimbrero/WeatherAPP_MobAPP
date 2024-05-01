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
import java.util.concurrent.ConcurrentHashMap

class A1SplashScreen : AppCompatActivity() {

    private val view by lazy { ActivityA1SplashScreenBinding.inflate(layoutInflater) }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }

        val scope = CoroutineScope(Dispatchers.IO)
        val jobs = mutableListOf<Job>()

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location ->
            val coordRequest = CityCoordinatesRequest(location.latitude, location.longitude)

            val locationJob = scope.launch {
                val apiResponse = fetchWeather(coordRequest)
                val cityName = coordRequest.getName()
                DataUtils.fillCurrentCity(apiResponse)
                DataUtils.setCurrentCityName(cityName)

                // Cargar ciudades predeterminadas
                val defaultCityJobs = DataUtils.defaultRequests.map { defaultRequest ->
                    launch {
                        val response = fetchWeather(defaultRequest)
                        DataUtils.fillCities(defaultRequest, response)
                    }
                }
                // Esperar que todas las ciudades predeterminadas se carguen
                defaultCityJobs.joinAll()

                // Inicializar usuario
                DataUtils.initUser()

                // Proceder a la actividad principal
                withContext(Dispatchers.Main) {
                    startActivity(Intent(this@A1SplashScreen, A2MainActivity::class.java))
                    finish()
                }
            }
            jobs.add(locationJob)
        }

        scope.launch {
            // Esperar a que la tarea de ubicación se complete
            jobs.joinAll()
        }
    }
}
