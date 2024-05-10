package com.example.weatherapp_mobapp

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp_mobapp.databinding.ActivityA6HistoricalDataBinding
import com.example.weatherapp_mobapp.weatherdb.DatabaseHelper
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class A6HistoricalData : AppCompatActivity() {

    private val view by lazy { ActivityA6HistoricalDataBinding.inflate(layoutInflater) }
    private var dbHandler: DatabaseHelper = DatabaseHelper(this)
    private lateinit var historicalUtils: HistoricalUtils
    private lateinit var cityName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        cityName = intent.getStringExtra("cityName")!!
        historicalUtils = HistoricalUtils(dbHandler.getAllCityForecasts(cityName))
        historicalUtils.printInformation() //To check if works
        println("Average Temperature: ${historicalUtils.averageTemperature()}")
        println("Sunny Days: ${historicalUtils.countSunnyDays()}")
        println("Max Wind Speed: ${historicalUtils.maxWindSpeed()}")
        println("Average Precipitation Probability: ${historicalUtils.averagePrecipitationProbability()}")
        setupHistoricalValues()
        setupCharts()
    }

    private fun setupHistoricalValues() {
        view.tvHistoricCityName.text = cityName
        view.tvAvgTemperatureValue.text = String.format("%.1f", historicalUtils.averageTemperature()).plus("º")
        view.tvSunnyDaysValue.text = historicalUtils.countSunnyDays().toString()
        view.tvMaxWindSpeedValue.text = historicalUtils.maxWindSpeed().toString().plus(" km/h")
        view.tvMinWindSpeedValue.text = historicalUtils.minWindSpeed().toString().plus(" km/h")
        view.tvAvgPrecipProbValue.text = String.format("%.1f", historicalUtils.averagePrecipitationProbability()).plus("%")
    }

    private fun setupCharts() {
        val chartMinTemp = findViewById<LineChart>(R.id.chartMinTemp)
        val chartMaxTemp = findViewById<LineChart>(R.id.chartMaxTemp)
        val boldTypeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        val entriesMin = historicalUtils.last15MinTemperatures()
        val entriesMax = historicalUtils.last15MaxTemperatures()

        // Configurar el dataset y el gráfico para la temperatura mínima
        val dataSetMin = LineDataSet(entriesMin, "Min Temperature")
        dataSetMin.color = Color.BLUE
        dataSetMin.valueTextSize = 10f // Ajustar el tamaño del texto de las etiquetas de los valores
        dataSetMin.valueTypeface = boldTypeface
        dataSetMin.setDrawValues(true) // Asegurarse de que los valores se dibujan en el gráfico
        val lineDataMin = LineData(dataSetMin)
        chartMinTemp.data = lineDataMin
        chartMinTemp.description.text = "Daily Minimum Temperatures"

        // Configurar el dataset y el gráfico para la temperatura máxima
        val dataSetMax = LineDataSet(entriesMax, "Max Temperature")
        dataSetMax.color = Color.RED
        dataSetMax.valueTextSize = 10f // Ajustar el tamaño del texto de las etiquetas de los valores
        dataSetMax.valueTypeface = boldTypeface
        dataSetMax.setDrawValues(true) // Asegurarse de que los valores se dibujan en el gráfico
        val lineDataMax = LineData(dataSetMax)
        chartMaxTemp.data = lineDataMax
        chartMaxTemp.description.text = "Daily Maximum Temperatures"

        // Refrescar los gráficos
        chartMinTemp.invalidate()
        chartMaxTemp.invalidate()
    }


}