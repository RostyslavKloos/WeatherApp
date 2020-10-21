package com.example.weatherapp.data.domain.model.forecastWeather

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ForecastResponse(
    val city: City?,
    val cnt: Int?,
    val cod: String?,
    val list: List<DayInfo>?,
    val message: Int?
) : Parcelable