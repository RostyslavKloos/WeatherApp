package com.example.weatherapp.data.domain.datasource

import com.example.weatherapp.data.local.dao.remote.IApiWeatherService

class ForecastRemoteDataSource(private val weatherService: IApiWeatherService) : BaseDataSource() {
    suspend fun getCurrentWeather(location: String, languageCode: String, metric: String) =
        getResult { weatherService.getCurrentWeather(location, languageCode, metric) }

    suspend fun getWeekWeather(location: String, languageCode: String, metric: String) =
        getResult { weatherService.getWeekWeather(location, languageCode, metric) }

    suspend fun getCurrentWeatherByLatLng(lat: String, lon: String, languageCode: String, metric: String) =
        getResult { weatherService.getCurrentWeatherByLatLng(lat, lon, languageCode, metric) }
}