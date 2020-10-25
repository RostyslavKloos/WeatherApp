package com.example.weatherapp.data.domain.datasource

import com.example.weatherapp.data.domain.remote.IApiWeatherService
import javax.inject.Inject

class CurrentWeatherRemoteDataSource @Inject constructor(private val weatherService: IApiWeatherService):
    BaseDataSource() {

    suspend fun getCurrentWeather(location: String, languageCode: String, metric: String) =
        getResult { weatherService.getCurrentWeather(location, languageCode, metric) }

    suspend fun getCurrentWeatherByLatLng(lat: String, lon: String, languageCode: String, metric: String) =
        getResult { weatherService.getCurrentWeatherByLatLng(lat, lon, languageCode, metric) }
}