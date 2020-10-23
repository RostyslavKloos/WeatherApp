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

const val API_KEY = "73809be7e323b58e2827364facf51e19"
const val BASE_URL = "http://api.openweathermap.org/data/2.5/"

//http://api.openweathermap.org/data/2.5/weather?q=Kiev&appid=73809be7e323b58e2827364facf51e19
//http://api.openweathermap.org/data/2.5/forecast?q=London&appid=73809be7e323b58e2827364facf51e19

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

    companion object {
        operator fun invoke(): IApiWeatherService {
            val requestInterceptor = Interceptor {
                val url = it.request()
                    .url
                    .newBuilder()
                    .addQueryParameter("appid", API_KEY)
                    .build()
                val request = it.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor it.proceed(request)
            }
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IApiWeatherService::class.java)
        }
    }
}