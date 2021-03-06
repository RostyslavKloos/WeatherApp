package com.example.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.domain.datasource.CurrentWeatherLocalDataSource
import com.example.weatherapp.data.domain.datasource.CurrentWeatherRemoteDataSource
import com.example.weatherapp.data.entities.CurrentWeatherEntity
import com.example.weatherapp.utils.performGetOperation
import javax.inject.Inject

class CurrentWeatherRepository @Inject constructor(private val currentWeatherRemoteDataSource: CurrentWeatherRemoteDataSource, private val currentWeatherLocalDataSource: CurrentWeatherLocalDataSource) {

     fun getCurrentWeather(location: String, languageCode: String, metric: String) = performGetOperation(
        databaseQuery = { currentWeatherLocalDataSource.getCurrentWeather() },
        networkCall = { currentWeatherRemoteDataSource.getCurrentWeather(location, languageCode, metric) },
        saveCallResult = {
            currentWeatherLocalDataSource.insertCurrentWeather(it)}
    )

     fun getCurrentWeatherByLatLng(lat: String, lon: String, languageCode: String, metric: String) = performGetOperation(
         databaseQuery = {currentWeatherLocalDataSource.getCurrentWeather() },
         networkCall = {currentWeatherRemoteDataSource.getCurrentWeatherByLatLng(lat, lon, languageCode, metric)},
         saveCallResult = {currentWeatherLocalDataSource.insertCurrentWeather(it)}
     )

    fun getCurrentWeatherFromDB(): LiveData<CurrentWeatherEntity> {
        return currentWeatherLocalDataSource.getCurrentWeather()
    }
}
