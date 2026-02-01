package com.example.weather_app.data.dto

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val current_weather: CurrentWeatherDto,
    val hourly: HourlyDto
)

data class CurrentWeatherDto(
    val temperature: Double,
    val windspeed: Double,
    val weathercode: Int,
    val time: String
)

data class HourlyDto(
    val time: List<String>,
    val temperature_2m: List<Double>,
    val relativehumidity_2m: List<Int>
)
