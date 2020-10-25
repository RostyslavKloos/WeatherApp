package com.example.weatherapp.data.domain.model.currentWeather

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CurrentWeatherResponse(

    val visibility: Int? = null,
    val timezone: Int? = null,
    val main: Main? = null,
    val clouds: Clouds? = null,
    val sys: Sys? = null,
    val dt: Int? = null,
    val coord: Coord? = null,
    val weather: List<Weather?>? = null,
    val name: String? = null,
    val cod: Int? = null,
    val id: Int? = null,
    val base: String? = null,
    val wind: Wind? = null
) : Parcelable
