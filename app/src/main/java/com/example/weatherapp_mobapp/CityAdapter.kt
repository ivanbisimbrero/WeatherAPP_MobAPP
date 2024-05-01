package com.example.weatherapp_mobapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CityAdapter(context: Context, private val cities: List<City>, private val displayType: String) : ArrayAdapter<City>(context, 0, cities) {

    companion object {
        const val TEMPERATURE = "Temperature"
        const val RAIN_PROB = "RainProb"
        const val CONDITION = "Condition"
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        val cityName = view.findViewById<TextView>(R.id.tvItem)
        val cityInfo = view.findViewById<TextView>(R.id.tvSubItem)

        val city = getItem(position)

        cityName.text = city?.name
        when (displayType) {
            TEMPERATURE -> cityInfo.text = city?.days?.get(0)?.temp.toString() + "º"
            RAIN_PROB -> cityInfo.text = city?.days?.get(0)?.precipprob.toString() + "%"
            CONDITION -> cityInfo.text = city?.days?.get(0)?.conditions
            else -> cityInfo.text = ""
        }

        return view
    }
}


