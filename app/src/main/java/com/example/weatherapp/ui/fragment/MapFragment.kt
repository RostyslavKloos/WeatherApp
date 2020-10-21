package com.example.weatherapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.MapFragmentBinding
import com.example.weatherapp.ui.viewmodel.MapViewModel
import com.example.weatherapp.utils.Resource
import com.example.weatherapp.utils.SharedManager
import com.example.weatherapp.utils.WeatherUseCase
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.coroutineContext

class MapFragment: Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var marker1: Marker

    private var _binding: MapFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MapViewModel
    private lateinit var sharedManager: SharedManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MapFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        sharedManager = SharedManager(requireContext())
        setUpViewModel()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

    }

    override fun onMapReady(p0: GoogleMap?) {
        setUpViewModel()
        p0?.apply {
            p0.setOnMapClickListener { latLng ->
                p0.clear()
                marker1 = p0.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(latLng.latitude.toString() + " : " + latLng.longitude)
                        .snippet("This is my spot!")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                )
                p0.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                p0.setOnMarkerClickListener(this@MapFragment)
            }
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        getCityName()
        return false
    }

    private fun getCityName(){
        viewModel.setCurrentWeatherParams(WeatherUseCase.WeatherCoordParams(marker1.position?.latitude.toString(),
            marker1.position?.longitude.toString(), "en", "metric"))
        viewModel.weather.observe(viewLifecycleOwner, {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        it.data?.let { currentWeather ->
                            val city = currentWeather.name

                            val action = MapFragmentDirections.actionMapFragmentToCurrentWeatherFragment(city)
                            Timber.tag("cityName").i(city + "BLABAL")
                            findNavController().navigate(action)
                        }
                    }
                    Resource.Status.ERROR -> {

                    }
                    Resource.Status.LOADING -> {

                    }
                }
            })
    }
}