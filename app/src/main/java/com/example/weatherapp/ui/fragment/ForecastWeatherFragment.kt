package com.example.weatherapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FutureWeatherFragmentBinding
import com.example.weatherapp.ui.adapter.ForecastAdapter
import com.example.weatherapp.ui.viewmodel.ForecastViewModel
import com.example.weatherapp.utils.Resource.Status.*
import com.example.weatherapp.utils.SharedManager
import com.example.weatherapp.utils.WeatherUseCase
import com.example.weatherapp.utils.dataTransform.ForecastMapper
import timber.log.Timber


class ForecastWeatherFragment : Fragment(), ForecastAdapter.WeatherItemListener {

    private var _binding: FutureWeatherFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ForecastViewModel
    private lateinit var adapter: ForecastAdapter
    private lateinit var sharedManager: SharedManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().actionBar?.setTitle(R.string.api_key)
        _binding = FutureWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        setupObservers()
    }

    private fun initViews() {
        sharedManager = SharedManager(requireContext())
        viewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)
        adapter = ForecastAdapter(this, null)
        binding.rvForecast.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObservers() {
        sharedManager.cityNameFlow.asLiveData().observe(viewLifecycleOwner, {
            val toolbar: Toolbar = (activity as MainActivity?)!!.findViewById(R.id.toolbar)
            toolbar.title = it

            viewModel.setCurrentWeatherParams(WeatherUseCase.WeatherParams(it, "en", "metric"))
            viewModel.forecast.observe(viewLifecycleOwner, {
                when (it.status) {
                    SUCCESS -> {
                        binding.rvForecast.visibility = View.VISIBLE
                        binding.futureProgressBar.visibility = View.GONE
                        it.data?.let {
                            val mappedList = it.list?.let { ForecastMapper().mapFrom(
                                it
                            )}
                            Timber.tag("lol").i(mappedList.toString())
                            adapter = ForecastAdapter(this, it)
                        }
                        binding.rvForecast.adapter = adapter
                    }
                    LOADING -> {
                        binding.rvForecast.visibility = View.GONE
                        binding.futureProgressBar.visibility = View.VISIBLE
                    }

                    ERROR -> {
                        binding.futureProgressBar.visibility = View.GONE
                        binding.rvForecast.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
        })
    }

    override fun onClickedWeather() {
        //findNavController().navigate(ForecastWeatherFragmentDirections.actionFutureWeatherFragmentToDetailWeatherFragment())
        Toast.makeText(requireContext(), "click", Toast.LENGTH_SHORT).show()
    }
}