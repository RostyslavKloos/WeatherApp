package com.example.weatherapp.data.domain.model.currentWeather


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Main(

    @Json(name = "temp")
    val temp: Double?,

    @Json(name = "temp_min")
    var tempMin: Double?,

    @Json(name = "grnd_level")
    val grndLevel: Double?,

    @Json(name = "temp_kf")
    val tempKf: Double?,

    @Json(name = "humidity")
    val humidity: Int?,

    @Json(name = "pressure")
    val pressure: Double?,

    @Json(name = "sea_level")
    val seaLevel: Double?,

    @Json(name = "temp_max")
    var tempMax: Double?
) : Parcelable {}