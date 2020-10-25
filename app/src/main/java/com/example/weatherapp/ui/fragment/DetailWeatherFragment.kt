package com.example.weatherapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weatherapp.data.domain.model.forecastWeather.DayInfo
import com.example.weatherapp.databinding.DetailWeatherFragmentBinding
import com.example.weatherapp.ui.adapter.WeatherHourOfDayAdapter
import com.example.weatherapp.ui.viewmodel.DetailWeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailWeatherFragment : Fragment() {

    private var _binding: DetailWeatherFragmentBinding? = null
    private val binding get() = _binding!!
    private val args: DetailWeatherFragmentArgs by navArgs()

    private val viewModel: DetailWeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.weatherItem.set(args.dayInfo)
        viewModel.selectedDayDate = args.dayInfo.dtTxt?.substringBefore(" ")
        viewModel.getForecast().observe(viewLifecycleOwner, {
            viewModel.selectedDayForecastLiveData.postValue(it.list?.filter {dayInfo ->
                dayInfo.dtTxt?.substringBefore(" ") == viewModel.selectedDayDate
            })
        })

        viewModel.selectedDayForecastLiveData.observe(viewLifecycleOwner, {
            initWeatherHourOfDayAdapter(it)
        })

        val item = viewModel.weatherItem.get()
        binding.textViewTemp.text = item?.main?.getTempString()
        binding.textViewDayOfWeek.text = item?.getDay()
        Glide.with(binding.imageViewForecastIcon)
            .load(item?.getWeatherItemValue())
            .into(binding.imageViewForecastIcon)
        binding.textViewMinTemp.text = item?.main?.getTempMinString()
        binding.textViewMaxTemp.text = item?.main?.getTempMaxString()

        binding.fabClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initWeatherHourOfDayAdapter(list: List<DayInfo>?) {
        val adapter = WeatherHourOfDayAdapter(list)

        binding.recyclerViewHourOfDay.adapter = adapter

    }
}