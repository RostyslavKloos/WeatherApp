package com.example.weatherapp.data.domain.remote

import com.example.weatherapp.data.domain.model.currentWeather.CurrentWeatherResponse
import com.example.weatherapp.data.domain.model.forecastWeather.ForecastResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface IApiWeatherService {

    @GET(value = "weather")
    suspend fun getCurrentWeather(
        @Query("q") location: String,
        @Query("lang") languageCode: String = "en",
        @Query("units") units: String = "metric"
    ): Response<CurrentWeatherResponse>

    @GET(value = "forecast")
    suspend fun getWeekWeather(
        @Query("q") location: String,
        @Query("lang") languageCode: String = "en",
        @Query("units") units: String = "metric"
    ): Response<ForecastResponse>

    @GET(value = "weather")
    suspend fun getCurrentWeatherByLatLng(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("lang") languageCode: String,
        @Query("units") metric: String
    ) : Response<CurrentWeatherResponse>

}