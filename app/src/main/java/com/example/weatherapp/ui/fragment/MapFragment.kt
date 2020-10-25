package com.example.weatherapp.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.MapFragmentBinding
import com.example.weatherapp.ui.viewmodel.MapViewModel
import com.example.weatherapp.utils.Resource
import com.example.weatherapp.utils.SharedManager
import com.example.weatherapp.utils.WeatherUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var marker1: Marker
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    private lateinit var map: GoogleMap
    private lateinit var mapFragment: SupportMapFragment

    private var _binding: MapFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapViewModel by viewModels()
    @Inject
    lateinit var sharedManager: SharedManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MapFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initMap()

        binding.fabCurrentLoc.setOnClickListener{
            fetchLocation()
        }
    }

    private fun initMap() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode
            )
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                drawMarker(currentLocation)
            }
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.apply {
            map = p0
            p0.setOnMapClickListener {
                    latLng ->
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


    private fun drawMarker(currentLocation: Location) {
        map.clear()
        val gps = LatLng(currentLocation.latitude, currentLocation.longitude)
        map.addMarker(
            MarkerOptions()
                .position(gps)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 10f))
        map.setOnMarkerClickListener {
            marker1 = it
            getCityName()
            false
        }
    }
    private fun getCityName() {
        viewModel.setCurrentWeatherParams(
            WeatherUseCase.WeatherCoordParams(
                marker1.position?.latitude.toString(),
                marker1.position?.longitude.toString(), "en", "metric"
            )
        )
        viewModel.weather.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let { currentWeather ->
                        currentWeather.name?.let {
                            MainScope().launch {
                                sharedManager.storeCity(it)
                            }
                            val action = MapFragmentDirections.actionMapFragmentToCurrentWeatherFragment()
                            findNavController().navigate(action)
                        }
                    }
                }
                Resource.Status.ERROR -> {

                }
                Resource.Status.LOADING -> {

                }
            }
        })
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        getCityName()
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                fetchLocation()
            }
        }
    }
}