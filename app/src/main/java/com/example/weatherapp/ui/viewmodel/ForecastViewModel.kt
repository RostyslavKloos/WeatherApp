package com.example.weatherapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.weatherapp.data.domain.datasource.ForecastLocalDataSource
import com.example.weatherapp.data.domain.remote.IApiWeatherService
import com.example.weatherapp.data.domain.datasource.ForecastRemoteDataSource
import com.example.weatherapp.data.local.db.ForecastDatabase
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.utils.WeatherUseCase
import com.example.weatherapp.utils.Resource
import com.example.weatherapp.data.entities.ForecastEntity

class ForecastViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService: IApiWeatherService = IApiWeatherService()
    private val forecastRemoteDataSource: ForecastRemoteDataSource = ForecastRemoteDataSource(apiService)
    private val notesDao = ForecastDatabase.getDatabase(application).forecastDao()
    private val forecastLocalDataSource = ForecastLocalDataSource(notesDao)
    private val repository = ForecastRepository(forecastRemoteDataSource, forecastLocalDataSource)


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