package com.example.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.domain.datasource.ForecastLocalDataSource
import com.example.weatherapp.data.domain.datasource.ForecastRemoteDataSource
import com.example.weatherapp.data.entities.ForecastEntity
import com.example.weatherapp.utils.performGetOperation
import javax.inject.Inject

class ForecastRepository @Inject constructor(
    private val forecastRemoteDataSource: ForecastRemoteDataSource,
    private val forecastLocalDataSource: ForecastLocalDataSource
) {

    fun getForecast(location: String, languageCode: String, metric: String) = performGetOperation(
        databaseQuery = {
            forecastLocalDataSource.getForecast()
        },
        networkCall = {
            forecastRemoteDataSource.getWeekWeather(location, languageCode, metric)
        },
        saveCallResult = {
            forecastLocalDataSource.insertForecast(it)
        }
    )

    fun getForecast(): LiveData<ForecastEntity> {
        return forecastLocalDataSource.getForecast()
    }
}