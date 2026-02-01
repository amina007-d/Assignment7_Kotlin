package com.example.weather_app.domain.model

data class Weather(
    val city: String,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val conditionCode: Int,
    val lastUpdate: String,
    val hourly: List<HourlyWeather>
)

data class HourlyWeather(
    val time: String,
    val temperature: Double
)
