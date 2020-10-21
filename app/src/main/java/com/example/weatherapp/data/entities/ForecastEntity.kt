package com.example.weatherapp.data.entities

import android.os.Parcelable
import androidx.room.*
import com.example.weatherapp.data.domain.model.forecastWeather.DayInfo
import com.example.weatherapp.data.domain.model.forecastWeather.ForecastResponse
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "Forecast")
data class ForecastEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @Embedded
    var city: CityEntity?,

    @ColumnInfo(name = "list")
    var list: List<DayInfo>?
) : Parcelable {

    @Ignore
    constructor(forecastResponse: ForecastResponse) : this(
        id = 0,
        city = forecastResponse.city?.let { CityEntity(it) },
        list = forecastResponse.list
    )
}
