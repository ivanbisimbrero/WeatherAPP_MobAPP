package com.example.weatherapp_mobapp

import android.view.View
import com.example.weatherapp_mobapp.databinding.ActivityA4CitiesListBinding

class A4CitiesList : BaseCityListActivity() {

    private val view by lazy { ActivityA4CitiesListBinding.inflate(layoutInflater) }
    override val cityUtils = SearchUtils(DataUtils.mainUser.cities)
    override val isFavouriteList = false

    override fun provideLayout(): View = view.root
    override fun provideListViewId(): Int = R.id.lvCities
    override fun provideSearchViewId(): Int = R.id.svSearchCity
    override fun provideRadioGroupId(): Int = R.id.rbgFilters
}