package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.data.domain.datasource.CurrentWeatherLocalDataSource
import com.example.weatherapp.data.domain.datasource.CurrentWeatherRemoteDataSource
import com.example.weatherapp.data.domain.datasource.ForecastLocalDataSource
import com.example.weatherapp.data.domain.datasource.ForecastRemoteDataSource
import com.example.weatherapp.data.domain.remote.IApiWeatherService
import com.example.weatherapp.data.local.dao.ICurrentWeatherDao
import com.example.weatherapp.data.local.dao.IForecastWeatherDao
import com.example.weatherapp.data.local.db.ForecastDatabase
import com.example.weatherapp.data.repository.CurrentWeatherRepository
import com.example.weatherapp.data.repository.ForecastRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val API_KEY = "73809be7e323b58e2827364facf51e19"
const val BASE_URL = "http://api.openweathermap.org/data/2.5/"

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson,okHttpClient: OkHttpClient) : Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(requestInterceptor: Interceptor, httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor{
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        return httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideRequestInterceptor(): Interceptor {
        return Interceptor {
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
    }

    @Provides
    fun provideWeatherApiService(retrofit: Retrofit): IApiWeatherService = retrofit.create(IApiWeatherService::class.java)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = ForecastDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideCurrentWeatherDao(db: ForecastDatabase) = db.currentDao()

    @Singleton
    @Provides
    fun provideForecastWeatherDao(db: ForecastDatabase) = db.forecastDao()

    @Singleton
    @Provides
    fun provideCurrentWeatherRepository(currentWeatherRemoteDataSource: CurrentWeatherRemoteDataSource, currentWeatherLocalDataSource: CurrentWeatherLocalDataSource) =
        CurrentWeatherRepository(currentWeatherRemoteDataSource, currentWeatherLocalDataSource)

    @Singleton
    @Provides
    fun provideForecastRepository(forecastRemoteDataSource: ForecastRemoteDataSource, forecastLocalDataSource: ForecastLocalDataSource) =
        ForecastRepository(forecastRemoteDataSource, forecastLocalDataSource)

    @Singleton
    @Provides
    fun provideCurrentWeatherLocalDataSource(currentWeatherDao: ICurrentWeatherDao) = CurrentWeatherLocalDataSource(currentWeatherDao)

    @Singleton
    @Provides
    fun provideCurrentWeatherRemoteDataSource(weatherService: IApiWeatherService) = CurrentWeatherRemoteDataSource(weatherService)

    @Singleton
    @Provides
    fun provideForecastLocalDataSource(forecastDao: IForecastWeatherDao) = ForecastLocalDataSource(forecastDao)

    @Singleton
    @Provides
    fun provideForecastRemoteDataSource(weatherService: IApiWeatherService) = ForecastRemoteDataSource(weatherService)

}