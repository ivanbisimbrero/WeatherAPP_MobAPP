package com.example.weatherapp_mobapp.requestCity

import com.example.weatherapp_mobapp.model.ApiResponse
import com.example.weatherapp_mobapp.utils.DataUtils
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

class CityCoordinatesRequest (
    val lat : Double,
    val lon : Double
) : WeatherRequest {

    var reverseApiUrl: String = "https://api.opencagedata.com/geocode/v1/json?q=${lat}%2C${lon}&key=${DataUtils.REVERSE_API_KEY}"

    override fun getURL() : String {
        return "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/${lat},${lon}?unitGroup=metric&key=${DataUtils.API_KEY}"
    }

    override suspend fun getName(): String {


        val client = HttpClient()
        val response: HttpResponse = client.get(reverseApiUrl)
        if (response.status == HttpStatusCode.OK) {
            val responseText: String = response.bodyAsText()
            // Parse the data
            val apiResponse = Gson().fromJson(responseText, ApiResponse::class.java)
            return apiResponse.results[0].components._normalized_city
        } else {
            return "A city"
        }


        //return "Your Location"
    }
}