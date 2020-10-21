package com.example.weatherapp.utils

class WeatherUseCase {
    class WeatherParams(
        val _location: String = "",
        val _languageCode: String = "en",
        val _units: String = "metric"
    )

    class WeatherCoordParams(
        val _latitude: String,
        val _longitude: String = "",
        val _languageCode: String = "en",
        val _units: String = "metric"
    )
}