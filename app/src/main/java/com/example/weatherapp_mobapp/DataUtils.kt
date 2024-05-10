package com.example.weatherapp_mobapp

import android.content.SharedPreferences
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp_mobapp.sharedPreferences.CrudAPI
import com.example.weatherapp_mobapp.sharedPreferences.SHARED_PREFERENCES_NAME
import com.example.weatherapp_mobapp.sharedPreferences.SharedPreferencesRepository
import com.example.weatherapp_mobapp.weatherdb.DatabaseHelper
import io.ktor.http.HttpStatusCode
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap


val API_KEY = "LRH7D4ZHU7LAWANGZBRQXPPGU" //Change if necessary
val REVERSE_API_KEY = "73ae2d27d6034e278eaae0007c703f28"
val USER_NAME = "ivanbisimbrero"
val USER_EMAIL = "alu.135046@usj.es"


enum class DefaultCities(val region: String) {
    Madrid("ES"),
    Barcelona("ES")
    //TODO: Add more cities
}

//These classes are the classes for the reverse geocoding with Gson
data class ApiResponse(
    val results: List<Result>
)

data class Result(
    val components: Components
)

data class Components(
    val _normalized_city: String
)
//End of the classes for reverse geocoding

data class User (
    val name: String,
    val email: String,
    val currentCity: City,
    val cities: MutableList<City>,
    var favCities: MutableList<City>
)

data class City (
    var name: String,
    val latitude: Double,
    val longitude: Double,
    val resolvedAddress: String,
    val days: MutableList<Forecast>,
    var isFavouriteCity: Boolean
)

data class Forecast (
    val datetime: String,
    val tempmax: Double,
    val tempmin: Double,
    val temp: Double,
    val feelslike: Double,
    val humidity: Double,
    val precipprob: Double,
    val windspeed: Double,
    val conditions: String,
    val icon: String
)

interface WeatherRequest {
    fun getURL(): String
    suspend fun getName(): String
}

data class CityCoordinatesRequest (
    val lat : Double,
    val lon : Double
) : WeatherRequest {

    var reverseApiUrl: String = "https://api.opencagedata.com/geocode/v1/json?q=${lat}%2C${lon}&key=${REVERSE_API_KEY}"

    override fun getURL() : String {
        return "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/${lat},${lon}?unitGroup=metric&key=${API_KEY}"
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

data class CityNameRequest (
    val defaultCity : DefaultCities
) : WeatherRequest {
    override fun getURL() : String {
        return "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/${defaultCity.name},${defaultCity.region}?unitGroup=metric&key=${API_KEY}"
    }

    override suspend fun getName(): String {
        return defaultCity.name
    }
}

suspend fun fetchWeather(request: WeatherRequest): String {
    val client = HttpClient()
    val response: HttpResponse = client.get(request.getURL())
    val responseText: String = response.bodyAsText()
    client.close()
    return responseText
}

// Function to deserialize the JSON string into a City object
private fun parseWeatherData(jsonResponse: String): City {
    // Parse the JSON response
    val gson = Gson()
    return gson.fromJson(jsonResponse, City::class.java)
}

object DataUtils {
    var defaultRequests: List<WeatherRequest> = listOf(
        CityNameRequest(DefaultCities.Madrid),
        CityNameRequest(DefaultCities.Barcelona)
    )

    lateinit var currentCity: City
    lateinit var cities: MutableList<City>
    lateinit var mainUser: User
    var citiesMap = ConcurrentHashMap<WeatherRequest, City>() //To avoid race conditions (Thanks teacher :))

    fun fillCurrentCity(dataFromAPI: String) {
        currentCity = parseWeatherData(dataFromAPI)
    }

    fun setCurrentCityName(name: String) {
        currentCity.name = name
    }

    suspend fun fillCities(request: WeatherRequest, dataFromAPI: String) {
        var auxCity: City = parseWeatherData(dataFromAPI)
        auxCity.name = request.getName()
        auxCity.isFavouriteCity = false
        citiesMap[request] = auxCity
    }

    fun initUser() {
        cities = citiesMap.values.toMutableList()
        mainUser = User(USER_NAME, USER_EMAIL, currentCity, cities, mutableListOf())

        println(mainUser.toString())
    }

    fun loadFavoriteCities(crudApi: CrudAPI) {
        val favoriteCityNames = crudApi.list()
        mainUser.favCities.clear()
        mainUser.cities.forEach { city ->
            if (favoriteCityNames.contains(city.name)) {
                city.isFavouriteCity = true
                mainUser.favCities.add(city)
            } else {
                city.isFavouriteCity = false
            }
        }
    }

    fun addUserForecastsToDatabase(dbHandler: DatabaseHelper) {
        mainUser.cities.forEach { city ->
            addFutureForecasts(city.days, city.name, dbHandler)
        }
    }
    private fun addFutureForecasts(forecasts: List<Forecast>, cityName: String, dbHandler: DatabaseHelper) {
        val currentDate = LocalDate.now()
        val futureForecasts = forecasts.filter { LocalDate.parse(it.datetime) >= currentDate }
        futureForecasts.forEach {
            dbHandler.addOrUpdateForecast(it, cityName)
        }
    }
}