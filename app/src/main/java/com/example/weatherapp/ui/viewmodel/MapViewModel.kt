package com.example.weatherapp.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.weatherapp.data.repository.CurrentWeatherRepository
import com.example.weatherapp.utils.Resource
import com.example.weatherapp.utils.WeatherUseCase
import com.example.weatherapp.data.entities.CurrentWeatherEntity

class MapViewModel @ViewModelInject constructor(private val repository: CurrentWeatherRepository) : ViewModel() {

    private val _params: MutableLiveData<WeatherUseCase.WeatherCoordParams> = MutableLiveData()

    fun setCurrentWeatherParams(params: WeatherUseCase.WeatherCoordParams) {
        if (_params.value == params)
            return
        _params.postValue(params)
    }

    private val _weather = _params.switchMap {
        repository.getCurrentWeatherByLatLng(it._latitude, it._longitude, it._languageCode, it._units)
    }

    val weather: LiveData<Resource<CurrentWeatherEntity>> = _weather
}