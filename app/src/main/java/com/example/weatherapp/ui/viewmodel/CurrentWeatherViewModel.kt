package com.example.weatherapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.weatherapp.data.domain.datasource.CurrentWeatherLocalDataSource
import com.example.weatherapp.data.local.dao.remote.IApiWeatherService
import com.example.weatherapp.data.domain.datasource.ForecastRemoteDataSource
import com.example.weatherapp.data.local.db.ForecastDatabase

import com.example.weatherapp.data.repository.CurrentWeatherRepository

import com.example.weatherapp.utils.WeatherUseCase
import com.example.weatherapp.utils.Resource
import com.example.weatherapp.data.entities.CurrentWeatherEntity

class CurrentWeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService: IApiWeatherService = IApiWeatherService()
    private val remoteDataSource: ForecastRemoteDataSource = ForecastRemoteDataSource(apiService)
    private val notesDao = ForecastDatabase.getDatabase(application).currentDao()
    private val currentWeatherLocalDataSource = CurrentWeatherLocalDataSource(notesDao)
    private val repository = CurrentWeatherRepository(remoteDataSource, currentWeatherLocalDataSource)

    private val _params: MutableLiveData<WeatherUseCase.WeatherParams> = MutableLiveData()

    fun setCurrentWeatherParams(params: WeatherUseCase.WeatherParams) {
        if (_params.value == params)
            return
        _params.postValue(params)

    }

    private val _weather = _params.switchMap {
        repository.getCurrentWeather(it._location, it._languageCode, it._units)
    }

    val weather: LiveData<Resource<CurrentWeatherEntity>> = _weather
}