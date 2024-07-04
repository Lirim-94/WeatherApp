package com.example.weatherapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun WeatherApp() {
    val viewModel: WeatherViewModel = viewModel()
    val weatherState by viewModel.weatherState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { viewModel.fetchWeather(52.52, 13.41) }) {
            Text("Get Weather for Berlin")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = weatherState) {
            is WeatherState.Loading -> CircularProgressIndicator()
            is WeatherState.Success -> {
                val data = state.data
                Text("Temperature: ${data.current_weather.temperature}°C")
                Text("Wind Speed: ${data.current_weather.windspeed} km/h")
                Text("Wind Direction: ${data.current_weather.winddirection}°")
                Text("Weather Code: ${data.current_weather.weathercode}")
                Text("Time: ${data.current_weather.time}")
            }
            is WeatherState.Error -> Text("Error: ${state.message}")
            WeatherState.Initial -> Text("Click the button to fetch weather data")
        }
    }
}