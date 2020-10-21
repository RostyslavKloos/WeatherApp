package com.example.weatherapp.utils

import androidx.room.TypeConverter
import com.example.weatherapp.data.domain.model.currentWeather.Weather
import com.example.weatherapp.data.domain.model.forecastWeather.DayInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * Created by Furkan on 2019-10-21
 */

object DataConverter {

    @TypeConverter
    @JvmStatic
    fun stringToList(data: String?): List<DayInfo>? {
        if (data == null) {
            return emptyList()
        }

        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, DayInfo::class.java)
        val adapter = moshi.adapter<List<DayInfo>>(type)
        return adapter.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun listToString(objects: List<DayInfo>): String {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, DayInfo::class.java)
        val adapter = moshi.adapter<List<DayInfo>>(type)
        return adapter.toJson(objects)
    }

    @TypeConverter
    @JvmStatic
    fun weatherStringToList(data: String?): List<Weather>? {
        if (data == null) {
            return emptyList()
        }

        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Weather::class.java)
        val adapter = moshi.adapter<List<Weather>>(type)
        return adapter.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun weatherListToString(objects: List<Weather>): String {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Weather::class.java)
        val adapter = moshi.adapter<List<Weather>>(type)
        return adapter.toJson(objects)
    }
}
