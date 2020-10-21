package com.example.weatherapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.data.local.dao.ICurrentWeatherDao
import com.example.weatherapp.data.local.dao.IForecastWeatherDao
import com.example.weatherapp.utils.DataConverter
import com.example.weatherapp.data.entities.CurrentWeatherEntity
import com.example.weatherapp.data.entities.ForecastEntity

@Database(entities = [CurrentWeatherEntity::class, ForecastEntity::class], version = 2)
@TypeConverters(DataConverter::class)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun currentDao(): ICurrentWeatherDao
    abstract fun forecastDao(): IForecastWeatherDao

    companion object {
        @Volatile private var instance: ForecastDatabase? = null

        fun getDatabase(context: Context): ForecastDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, ForecastDatabase::class.java, "forecast.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}