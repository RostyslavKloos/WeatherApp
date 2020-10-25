package com.example.weatherapp.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.example.weatherapp.data.domain.model.currentWeather.Sys
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Sys")
data class SysEntity(
    @ColumnInfo(name = "country")
    val country: String?,
    @ColumnInfo(name = "sunrise")
    val sunrise: Int?,
    @ColumnInfo(name = "sunset")
    val sunset: Int?,
    @ColumnInfo(name = "type")
    val type: Int?
) : Parcelable {
    @Ignore
    constructor(sys: Sys?) : this(
        country = sys?.country,
        sunrise = sys?.sunrise,
        sunset = sys?.sunset,
        type = sys?.type
    )
}