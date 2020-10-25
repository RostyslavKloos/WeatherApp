package com.example.weatherapp.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.utils.WeatherUseCase
import com.example.weatherapp.utils.Resource
import com.example.weatherapp.data.entities.ForecastEntity

class ForecastViewModel @ViewModelInject constructor(private val repository: ForecastRepository) : ViewModel() {

    private val _params: MutableLiveData<WeatherUseCase.WeatherParams> = MutableLiveData()

    fun setCurrentWeatherParams(params: WeatherUseCase.WeatherParams) {
        if (_params.value == params)
            return
        _params.postValue(params)
    }

    private val forecastViewState = _params.switchMap {
        repository.getForecast(it._location, it._languageCode, it._units)
    }

    val forecast: LiveData<Resource<ForecastEntity>> = forecastViewState

}