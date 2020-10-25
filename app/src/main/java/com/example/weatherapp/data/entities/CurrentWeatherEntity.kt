package com.example.weatherapp.data.entities

import android.os.Parcelable
import androidx.room.*
import com.example.weatherapp.data.domain.model.currentWeather.CurrentWeatherResponse
import com.example.weatherapp.data.domain.model.currentWeather.Weather
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "CurrentWeather")
data class CurrentWeatherEntity(
    @ColumnInfo(name = "visibility")
    var visibility: Int?,
    @ColumnInfo(name = "timezone")
    var timezone: Int?,
    @Embedded
    var main: MainEntity?,
    @Embedded
    var clouds: CloudsEntity?,
    @ColumnInfo(name = "dt")
    var dt: Long?,
    @ColumnInfo(name = "weather")
    val weather: List<Weather?>? = null,
    @Embedded
    val sys: SysEntity?,
    @ColumnInfo(name = "name")
    val name: String?,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "base")
    val base: String?,
    @Embedded
    val wind: WindEntity?
) : Parcelable {

    @Ignore
    constructor(currentWeather: CurrentWeatherResponse) : this(
        visibility = currentWeather.visibility,
        timezone = currentWeather.timezone,
        main = MainEntity(currentWeather.main),
        clouds = CloudsEntity(currentWeather.clouds),
        dt = currentWeather.dt?.toLong(),
        weather = currentWeather.weather,
        sys = SysEntity(currentWeather.sys),
        name = currentWeather.name,
        id = 0,
        base = currentWeather.base,
        wind = WindEntity(currentWeather.wind)
    )

    private fun getCurrentWeather(): Weather? {
        return weather?.first()
    }

    fun getCurrentWeatherIconValue(): String? {
        return StringBuilder("http://openweathermap.org/img/wn/")
            .append(getCurrentWeather()?.icon)
            .append(".png")
            .toString()
    }
}
