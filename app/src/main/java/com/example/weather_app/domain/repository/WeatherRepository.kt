package com.example.weather_app.domain.repository
import com.example.weather_app.data.repository.WeatherResult

interface WeatherRepository {
    suspend fun getWeather(
        city: String,
        lat: Double,
        lon: Double
    ): WeatherResult
}
