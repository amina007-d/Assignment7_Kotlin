package com.example.weather_app.presentation.state

import com.example.weather_app.domain.model.Weather

data class WeatherUiState(
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val error: String? = null,
    val isOffline: Boolean = false
)
