package com.example.weatherapp.network

import WeatherResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.request.*
import io.ktor.client.call.*
import kotlinx.serialization.json.Json


object WeatherApi {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
           json(
               Json {
                   ignoreUnknownKeys = true
               }
           )
        }
    }

    suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponse {
        return client.get("https://api.open-meteo.com/v1/forecast") {
            url {
                parameters.append("latitude", latitude.toString())
                parameters.append("longitude", longitude.toString())
                parameters.append("current_weather", "true")
            }
        }.body()
    }
}