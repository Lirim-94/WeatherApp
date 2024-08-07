package com.example.weatherapp.ui

import WeatherResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.network.GeocodingApi
import com.example.weatherapp.network.WeatherApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val data: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
    object Initial : WeatherState()
}

sealed class WeatherIntent {
    data class FetchWeather(val location: String) : WeatherIntent()
}

class WeatherViewModel : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Initial)
    val weatherState: StateFlow<WeatherState> = _weatherState

    fun handleIntent(intent: WeatherIntent) {
        when (intent) {
            is WeatherIntent.FetchWeather -> fetchWeatherForLocation(intent.location)
        }
    }

    fun fetchWeatherForLocation(location: String) {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading
            try {
                val (latitude, longitude) = getCoordinatesForLocation(location)
                val weather = WeatherApi.getWeather(latitude, longitude)
                _weatherState.value = WeatherState.Success(weather)

            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    private suspend fun getCoordinatesForLocation(location: String) : Pair<Double, Double> {
                return GeocodingApi.getCoordinates(location)
            }
}