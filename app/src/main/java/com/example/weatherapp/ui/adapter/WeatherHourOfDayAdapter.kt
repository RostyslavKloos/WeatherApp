package com.example.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.domain.model.forecastWeather.DayInfo
import com.example.weatherapp.databinding.ItemWeatherHourOfDayBinding

class WeatherHourOfDayAdapter(private val list: List<DayInfo>?):
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemWeatherHourOfDayBinding = ItemWeatherHourOfDayBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list!![position])

    override fun getItemCount(): Int {
        return list?.size!!
    }
}

class ViewHolder(private val itemBinding: ItemWeatherHourOfDayBinding): RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(dayInfo: DayInfo) {
        itemBinding.textViewHourOfDay.text = dayInfo.getHourOfDay()
        itemBinding.textViewTemp.text = dayInfo.main?.getTempString()
    }
}