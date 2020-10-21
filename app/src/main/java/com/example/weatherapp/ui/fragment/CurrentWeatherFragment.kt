package com.example.weatherapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.CurrentWeatherFragmentBinding
import com.example.weatherapp.ui.viewmodel.CurrentWeatherViewModel
import com.example.weatherapp.utils.WeatherUseCase
import com.example.weatherapp.utils.Resource.Status.*
import com.example.weatherapp.utils.SharedManager
import com.example.weatherapp.data.entities.CurrentWeatherEntity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import timber.log.Timber


class CurrentWeatherFragment : Fragment() {
    private var _binding: CurrentWeatherFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var autocompleteCityName: String
    private lateinit var sharedManager: SharedManager
    private val args: CurrentWeatherFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CurrentWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Places.initialize(requireContext(), "AIzaSyB2GgEPRzXyIcJ3wHhhSc9rwsUQQ22Vhqo")
        Timber.tag("lifecycle").i("OnActivityCreated")

        setUpViewModel()
        setupUI()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this).get(CurrentWeatherViewModel::class.java)
    }

    private fun setupUI() {

        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES)
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        sharedManager = SharedManager(requireContext())


        args.city?.let{
            //refreshData(it)
            //autocompleteFragment.setText(it)
            Timber.tag("cityName").i(it)
        }

//        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//            override fun onPlaceSelected(p0: Place) {
//
//                GlobalScope.launch {
//                    sharedManager.storeCity(p0.name.toString())
//                }
//
//                autocompleteCityName = p0.name.toString()
//
//                refreshData(autocompleteCityName)
//            }
//
//            override fun onError(p0: Status) {
//                Timber.i("An error occurred: $p0")
//            }
//        })
    }

    private fun refreshData(city: String) {
        viewModel.setCurrentWeatherParams(WeatherUseCase.WeatherParams(city, "en", "metric"))
        viewModel.weather.observe(viewLifecycleOwner, {
            when (it.status) {
                SUCCESS -> {
                    it.data?.let { it1 -> bindWeather(it1) }
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
        binding.rlWeather.visibility = View.VISIBLE
        binding.tvCountry.text = weather.sys?.country
        binding.tvCity.text = weather.name
        Glide.with(binding.imgIcon).load(
            StringBuilder("http://openweathermap.org/img/wn/")
                .append(weather.weather?.get(0)?.icon).append(".png")
                .toString()
            ).into(binding.imgIcon)
        binding.tvTempValue.text = weather.main?.temp.toString()
    }

//    override fun onResume() {
//        super.onResume()
//        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
//        sharedManager.cityNameFlow.asLiveData().observe( viewLifecycleOwner, {
//            autocompleteFragment.setText(it)
//            refreshData(it)
//        })
//        Timber.tag("lifecycle").i("OnResume")
//    }

    override fun onStop() {
        super.onStop()
        Timber.tag("lifecycle").i("OnStop")
    }

    override fun onStart() {
        super.onStart()
        Timber.tag("lifecycle").i("OnStart")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}