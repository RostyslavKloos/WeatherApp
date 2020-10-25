package com.example.weatherapp.data.domain.datasource

import com.example.weatherapp.data.domain.remote.IApiWeatherService
import javax.inject.Inject

class ForecastRemoteDataSource @Inject constructor(private val weatherService: IApiWeatherService) : BaseDataSource() {

    suspend fun getWeekWeather(location: String, languageCode: String, metric: String) =
        getResult { weatherService.getWeekWeather(location, languageCode, metric) }

}