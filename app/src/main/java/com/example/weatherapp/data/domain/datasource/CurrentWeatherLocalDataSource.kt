package com.example.weatherapp.data.domain.datasource

import com.example.weatherapp.data.domain.model.currentWeather.CurrentWeatherResponse
import com.example.weatherapp.data.local.dao.ICurrentWeatherDao
import com.example.weatherapp.data.entities.CurrentWeatherEntity
import javax.inject.Inject

class CurrentWeatherLocalDataSource @Inject constructor(private val currentWeatherDao: ICurrentWeatherDao){
     fun getCurrentWeather() = currentWeatherDao.getCurrentWeather()
     fun insertCurrentWeather(currentWeatherResponse: CurrentWeatherResponse) = currentWeatherDao.deleteAndInsert(
        CurrentWeatherEntity(currentWeatherResponse)
    )

    fun deleteCurrentWeather() = currentWeatherDao.deleteCurrentWeather()
}