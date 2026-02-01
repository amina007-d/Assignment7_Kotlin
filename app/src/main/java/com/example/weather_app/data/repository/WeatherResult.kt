package com.example.weather_app.data.repository

import com.example.weather_app.domain.model.Weather

sealed class WeatherResult {
    data class Success(
        val weather: Weather,
        val isFromCache: Boolean
    ) : WeatherResult()

    data class Error(
        val throwable: Throwable
    ) : WeatherResult()
}

