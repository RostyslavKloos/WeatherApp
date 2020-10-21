package com.example.weatherapp.data.domain.model.forecastWeather


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Sys(
    @Json(name = "pod")
    val pod: String?
) : Parcelable