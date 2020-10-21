package com.example.weatherapp.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.ui.viewmodel.DetailWeatherViewModel

class DetailWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = DetailWeatherFragment()
    }

    private lateinit var viewModel: DetailWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

}