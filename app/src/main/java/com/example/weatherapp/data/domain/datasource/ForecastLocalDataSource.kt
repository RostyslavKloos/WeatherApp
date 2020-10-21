package com.example.weatherapp.data.domain.datasource

import com.example.weatherapp.data.domain.model.forecastWeather.ForecastResponse
import com.example.weatherapp.data.local.dao.IForecastWeatherDao
import com.example.weatherapp.data.entities.ForecastEntity

class ForecastLocalDataSource(private val forecastDao: IForecastWeatherDao): BaseDataSource(){
    fun getForecast() = forecastDao.getForecast()

    fun insertForecast(forecastResponse: ForecastResponse) = forecastDao.deleteAndInsert(
        ForecastEntity(forecastResponse)
    )

}