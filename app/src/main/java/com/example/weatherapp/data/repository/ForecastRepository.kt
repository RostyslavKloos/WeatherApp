package com.example.weatherapp.data.repository

import com.example.weatherapp.data.domain.datasource.ForecastLocalDataSource
import com.example.weatherapp.data.domain.datasource.ForecastRemoteDataSource
import com.example.weatherapp.utils.performGetOperation

class ForecastRepository(
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
}