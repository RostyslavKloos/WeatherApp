package com.example.weatherapp.ui.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.domain.datasource.ForecastLocalDataSource
import com.example.weatherapp.data.domain.datasource.ForecastRemoteDataSource
import com.example.weatherapp.data.domain.model.forecastWeather.DayInfo
import com.example.weatherapp.data.domain.remote.IApiWeatherService
import com.example.weatherapp.data.entities.ForecastEntity
import com.example.weatherapp.data.local.db.ForecastDatabase
import com.example.weatherapp.data.repository.ForecastRepository

class DetailWeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService: IApiWeatherService = IApiWeatherService()
    private val forecastRemoteDataSource: ForecastRemoteDataSource = ForecastRemoteDataSource(apiService)
    private val notesDao = ForecastDatabase.getDatabase(application).forecastDao()
    private val forecastLocalDataSource = ForecastLocalDataSource(notesDao)
    private val repository = ForecastRepository(forecastRemoteDataSource, forecastLocalDataSource)

    val test = "Test"
    var weatherItem: ObservableField<DayInfo> = ObservableField()
    var selectedDayDate: String? = null
    var selectedDayForecastLiveData: MutableLiveData<List<DayInfo>> = MutableLiveData()

    fun getForecast(): LiveData<ForecastEntity> {
        return forecastLocalDataSource.getForecast()
    }

}