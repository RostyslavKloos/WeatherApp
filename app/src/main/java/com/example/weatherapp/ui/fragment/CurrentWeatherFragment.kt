package com.example.weatherapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.CurrentWeatherFragmentBinding
import com.example.weatherapp.ui.viewmodel.CurrentWeatherViewModel
import com.example.weatherapp.utils.WeatherUseCase
import com.example.weatherapp.utils.Resource.Status.*
import com.example.weatherapp.utils.SharedManager
import com.example.weatherapp.data.entities.CurrentWeatherEntity
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber


class CurrentWeatherFragment : Fragment() {
    private var _binding: CurrentWeatherFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var autocompleteCityName: String
    private lateinit var sharedManager: SharedManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CurrentWeatherFragmentBinding.inflate(inflater, container, false)
        Places.initialize(requireContext(), "AIzaSyB2GgEPRzXyIcJ3wHhhSc9rwsUQQ22Vhqo")
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViewModel()
        catchMapFragmentData()
        setupUI()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this).get(CurrentWeatherViewModel::class.java)
    }

    private fun catchMapFragmentData() {
        viewModel.fetchData().observe(viewLifecycleOwner, {
            it?.let {
                bindWeather(it)
                it.name?.let {
                    GlobalScope.launch {
                        sharedManager.storeCity(it)
                    }
                }
            }
        })
    }

    private fun setupUI() {
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES)
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        sharedManager = SharedManager(requireContext())

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {

                MainScope().launch {
                    sharedManager.storeCity(p0.name.toString())
                }

                autocompleteCityName = p0.name.toString()

                refreshData(autocompleteCityName)
            }

            override fun onError(p0: Status) {
                Timber.i("An error occurred: $p0")

            }
        })
    }

    private fun refreshData(city: String) {
        viewModel.setCurrentWeatherParams(WeatherUseCase.WeatherParams(city, "en", "metric"))
        viewModel.weather.observe(viewLifecycleOwner, {
            when (it.status) {
                SUCCESS -> {
                    it.data?.let {
                        bindWeather(it)
                    }
                }

                LOADING -> {
                    binding.rlWeather.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }

                ERROR -> {
                    binding.rlWeather.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun bindWeather(weather: CurrentWeatherEntity) {
        binding.tvCountry.text = weather.sys?.country
        binding.tvCity.text = weather.name
        Glide.with(binding.imgIcon).load(weather.getCurrentWeatherIconValue())
            .into(binding.imgIcon)
        binding.tvTempValue.text = weather.main?.getTempString()
        binding.tvValueFeelsLike.text = weather.main?.getFeelsLikeString()
        binding.tvValueHumidity.text = weather.main?.getHumidityString()
        binding.tvValuePressure.text = weather.main?.pressure.toString()
        binding.rlWeather.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}