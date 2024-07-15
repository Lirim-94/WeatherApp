package com.example.weatherapp.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonPrimitive

object GeocodingApi {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
    suspend fun getCoordinates(location: String) : Pair<Double, Double> {
        val response: HttpResponse = client.get("https://nominatim.openstreetmap.org/search") {
            url {
                parameters.append("q", location)
                parameters.append("format", "json")
                parameters.append("limit", "1")
            }
        }
        val jsonArray = Json.decodeFromString<JsonArray>(response.bodyAsText())
        val firstResult = jsonArray.firstOrNull() as? JsonObject
            ?: throw Exception("Location not found")

        val lat = firstResult["lat"]?.jsonPrimitive?.doubleOrNull
            ?: throw Exception("Latitude not found")
        val lon = firstResult["lon"]?.jsonPrimitive?.doubleOrNull
            ?: throw Exception("Longitude not found")

        return Pair(lat, lon)
    }


}