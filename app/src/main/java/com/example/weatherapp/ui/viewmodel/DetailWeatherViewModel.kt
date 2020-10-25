package com.example.weatherapp.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.domain.model.forecastWeather.DayInfo
import com.example.weatherapp.data.entities.ForecastEntity
import com.example.weatherapp.data.repository.ForecastRepository

class DetailWeatherViewModel @ViewModelInject constructor(private val repository: ForecastRepository) : ViewModel() {

    var weatherItem: ObservableField<DayInfo> = ObservableField()
    var selectedDayDate: String? = null
    var selectedDayForecastLiveData: MutableLiveData<List<DayInfo>> = MutableLiveData()

    fun getForecast(): LiveData<ForecastEntity> {
        return repository.getForecast()
    }
}