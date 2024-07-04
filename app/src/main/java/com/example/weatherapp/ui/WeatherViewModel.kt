package com.example.weatherapp.ui

import WeatherResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class WeatherViewModel : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Initial)
    val weatherState: StateFlow<WeatherState> = _weatherState

    fun fetchWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading
            try {
                val weather = WeatherApi.getWeather(latitude, longitude)
                _weatherState.value = WeatherState.Success(weather)
                println("Weather fetched successfully: $weather") // Debug log
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error(e.message ?: "Unknown error occurred")
                println("Error fetching weather: ${e.message}") // Debug log
            }
        }
    }
}