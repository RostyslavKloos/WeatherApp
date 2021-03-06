package com.example.weatherapp.data.domain.model.forecastWeather

import android.os.Parcelable
import android.util.Log
import com.example.weatherapp.data.domain.model.currentWeather.Weather
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Parcelize
@JsonClass(generateAdapter = true)
data class DayInfo(
    val clouds: Clouds?,
    val dt: Long?,
    @SerializedName("dt_txt")
    val dtTxt: String?,
    val main: Main?,
    val pop: Double?,
    val rain: Rain?,
    val sys: Sys?,
    val visibility: Int?,
    val weather: List<Weather?>?,
    val wind: Wind?
) : Parcelable {

    private fun getWeatherItem(): Weather? {
        return weather?.first()
    }
    fun getWeatherItemValue(): String? {
        return StringBuilder("http://openweathermap.org/img/wn/")
            .append(getWeatherItem()?.icon)
            .append(".png")
            .toString()
    }

    fun getDay(): String? {
        return dt?.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                getDateTime(it)?.getDisplayName(TextStyle.FULL, Locale.getDefault())
            } else {
                getDateTimeForLowApi(it)
            }
        }
    }

    private fun getDateTimeForLowApi(s: Long): String {
        return try {
            val sdf = SimpleDateFormat("EEEE", Locale.ROOT)
            val netDate = Date(s * 1000)
            val formattedDate = sdf.format(netDate)

            formattedDate
        }
        catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun getDateTime(s: Long): DayOfWeek? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
            val netDate = Date(s * 1000)
            val formattedDate = sdf.format(netDate)

            LocalDate.of(
                formattedDate.substringAfterLast("/").toInt(),
                formattedDate.substringAfter("/").take(2).toInt(),
                formattedDate.substringBefore("/").toInt()
            ).dayOfWeek

        } catch (e: Exception) {
            e.printStackTrace()
            DayOfWeek.MONDAY
        }
    }

    fun getHourOfDay(): String {
        return dtTxt?.substringAfter(" ")?.substringBeforeLast(":") ?: "00:00"
    }

    fun getDayDate(): String {
        return dtTxt?.substringBefore(" ") ?: ""
    }
}