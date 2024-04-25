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
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readText
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class A1SplashScreen : AppCompatActivity() {

    private val view by lazy { ActivityA1SplashScreenBinding.inflate(layoutInflater) }
    private val scope = CoroutineScope(Dispatchers.IO)
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
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location ->
                println("Latitud: " + location.latitude)
                println("Longitud: " + location.longitude)

                scope.launch {
                    //val weatherData = fetchWeather(location.latitude, location.longitude)

                    //TODO: manage the returned data
                    //println(weatherData)

                    withContext(Dispatchers.Main) {
                        startActivity(Intent(this@A1SplashScreen, A2MainActivity::class.java))
                        finish()
                    }
                }
            }
    }

    private suspend fun fetchWeather(lat: Double, lon: Double): String {
        val client = HttpClient()
        val response: HttpResponse = client.get("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/$lat,$lon?unitGroup=metric&key=LRH7D4ZHU7LAWANGZBRQXPPGU")
        val responseText: String = response.bodyAsText()
        client.close()
        return responseText
    }
}