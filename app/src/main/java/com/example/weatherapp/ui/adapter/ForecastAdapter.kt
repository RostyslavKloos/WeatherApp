package com.example.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.data.domain.model.forecastWeather.DayInfo
import com.example.weatherapp.databinding.ItemForecastWeatherBinding

class ForecastAdapter(
    private val listener: WeatherItemListener,
    private val forecast: List<DayInfo>?
) : RecyclerView.Adapter<ForecastViewHolder>() {

    interface WeatherItemListener {
        fun onClickedWeather(currentDay: DayInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding: ItemForecastWeatherBinding =
            ItemForecastWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) = holder.bind(forecast!![position])

    override fun getItemCount(): Int {
        return forecast!!.size
    }
}

class ForecastViewHolder(
    private val itemBinding: ItemForecastWeatherBinding,
    private val listener: ForecastAdapter.WeatherItemListener
) : RecyclerView.ViewHolder(itemBinding.root),
    View.OnClickListener {

    init {
        itemBinding.root.setOnClickListener(this)
    }

    private lateinit var currentDay: DayInfo

    fun bind(item: DayInfo) {
        this.currentDay = item
        itemBinding.tvDateWeekDay.text = item.getDay()
        itemBinding.tvDate.text = item.getDayDate()
        itemBinding.tvTempMin.text = item.main?.getTempMinString()
        itemBinding.tvTempMax.text = item.main?.getTempMaxString()
        itemBinding.tvDescription.text = item.weather?.get(0)?.description
        Glide.with(itemBinding.imgForecastIcon)
            .load(item.getWeatherItemValue())
            .into(itemBinding.imgForecastIcon)
    }

    override fun onClick(v: View?) {
        listener.onClickedWeather(currentDay)
    }
}






