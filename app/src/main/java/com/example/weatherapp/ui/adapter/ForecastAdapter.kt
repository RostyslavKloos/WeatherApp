package com.example.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.data.domain.model.forecastWeather.DayInfo
import com.example.weatherapp.databinding.FutureWeatherItemBinding
import com.example.weatherapp.data.entities.ForecastEntity
import kotlinx.android.synthetic.main.future_weather_item.view.*
import kotlin.math.roundToInt

class ForecastAdapter(
    private val listener: WeatherItemListener,
    private val forecast: ForecastEntity?
) : RecyclerView.Adapter<ForecastViewHolder>() {

    private val list = mutableListOf<DayInfo>()
    private val tempMaxList = mutableListOf<Double>()
    private val tempMinList = mutableListOf<Double>()
    private var mappedList: List<DayInfo>? = null

    interface WeatherItemListener {
        fun onClickedWeather()
    }

    private fun transformDays() {
        list.clear()
        var tempMin = 0.0
        var tempMax = 0.0
        var countMin = 0
        var countMax = 0
        forecast?.list?.indices?.let {
            for (item in forecast?.list?.indices!!) {
                val current = forecast.list!![item]

                when {
                    item == forecast.list!!.lastIndex -> {

                        tempMin += forecast.list!![item].main?.tempMin!!
                        countMin++
                        tempMax += forecast.list!![item].main?.tempMax!!
                        countMax++
                        tempMin /= countMin.toDouble()
                        tempMax /= countMax.toDouble()

                        tempMaxList.add(tempMax)
                        tempMinList.add(tempMin)
                        list.add(current)
                        return
                    }

                    current.dtTxt?.substring(8, 10) == forecast.list!![item + 1].dtTxt?.substring(
                        8,
                        10
                    ) -> {
                        tempMin += current.main?.tempMin!!
                        countMin++
                        tempMax += current.main.tempMax!!
                        countMax++
                    }

                    current.dtTxt?.substring(8, 10) != forecast.list!![item + 1].dtTxt?.substring(
                        8,
                        10
                    ) -> {
                        tempMin += current.main?.tempMin!!
                        countMin++
                        tempMax += current.main.tempMax!!
                        countMax++

                        tempMin /= countMin.toDouble()
                        tempMax /= countMax.toDouble()

                        tempMaxList.add(tempMax)
                        tempMinList.add(tempMin)

                        tempMax = 0.0
                        countMax = 0
                        tempMin = 0.0
                        countMin = 0

                        list.add(current)
                    }
                }
            }

            list.add(forecast.list!![forecast.list!!.size - 1])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding: FutureWeatherItemBinding =
            FutureWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.itemView.tv_dateTime.text = list[position].dtTxt?.substring(0, 10)
        holder.itemView.tv_tempMin.text = "${tempMinList[position].roundToInt()}"
        holder.itemView.tv_tempMax.text =  "${tempMaxList[position].roundToInt()}"
        Glide.with(holder.itemView.img_ForecastIcon).load(
            StringBuilder("http://openweathermap.org/img/wn/")
                .append(list[position].weather?.get(0)?.icon)
                .append(".png").toString()
        ).into(holder.itemView.img_ForecastIcon)
        holder.itemView.tvDescription.text = list[position].weather?.get(0)?.description
    }

    override fun getItemCount(): Int {
        transformDays()
        return list.size
    }
}

class ForecastViewHolder(
    private val itemBinding: FutureWeatherItemBinding,
    private val listener: ForecastAdapter.WeatherItemListener
) : RecyclerView.ViewHolder(itemBinding.root),
    View.OnClickListener {

    init {
        itemBinding.root.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        listener.onClickedWeather()
    }
}