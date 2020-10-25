package com.example.weatherapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.data.domain.model.forecastWeather.DayInfo
import com.example.weatherapp.databinding.ForecastWeatherFragmentBinding
import com.example.weatherapp.ui.adapter.ForecastAdapter
import com.example.weatherapp.ui.viewmodel.ForecastViewModel
import com.example.weatherapp.utils.Resource.Status.*
import com.example.weatherapp.utils.SharedManager
import com.example.weatherapp.utils.WeatherUseCase
import com.example.weatherapp.utils.dataTransform.ForecastMapper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ForecastWeatherFragment : Fragment(), ForecastAdapter.WeatherItemListener {

    private var _binding: ForecastWeatherFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ForecastViewModel by viewModels()
    private lateinit var adapter: ForecastAdapter
    @Inject
    lateinit var sharedManager: SharedManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().actionBar?.setTitle(R.string.api_key)
        _binding = ForecastWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        setupObservers()
    }

    private fun initViews() {
        adapter = ForecastAdapter(this, emptyList())
        binding.rvForecast.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObservers() {
        sharedManager.cityNameFlow.asLiveData().observe(viewLifecycleOwner, { cityName ->
            val toolbar: Toolbar = (activity as MainActivity?)!!.findViewById(R.id.toolbar)
            toolbar.title = cityName

            viewModel.setCurrentWeatherParams(WeatherUseCase.WeatherParams(cityName, "en", "metric"))
            viewModel.forecast.observe(viewLifecycleOwner, { forecastResource ->
                when (forecastResource.status) {
                    SUCCESS -> {
                        forecastResource.data?.let { forecastEntity ->
                            val mappedList = forecastEntity.list?.let { dayInfo ->
                                ForecastMapper().mapFrom(dayInfo)}
                            adapter = ForecastAdapter(this, mappedList ?: emptyList())
                        }
                        binding.rvForecast.adapter = adapter
                        binding.rvForecast.visibility = View.VISIBLE
                        binding.futureProgressBar.visibility = View.GONE
                    }
                    LOADING -> {
                        binding.rvForecast.visibility = View.GONE
                        binding.futureProgressBar.visibility = View.VISIBLE
                    }

                    ERROR -> {
                        binding.futureProgressBar.visibility = View.GONE
                        binding.rvForecast.visibility = View.GONE
                        Toast.makeText(requireContext(), forecastResource.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
        })
    }
    override fun onClickedWeather(currentDay: DayInfo) {
        findNavController().navigate(ForecastWeatherFragmentDirections.actionFutureWeatherFragmentToDetailWeatherFragment(currentDay))
    }
}