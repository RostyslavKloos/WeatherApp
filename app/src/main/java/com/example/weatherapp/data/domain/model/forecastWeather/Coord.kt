package com.example.weatherapp.data.domain.model.forecastWeather

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Coord(
    val lat: Double?,
    val lon: Double?
) : Parcelable