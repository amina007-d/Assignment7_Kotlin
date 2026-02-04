package com.example.weather_app.domain.model

data class FavoriteCity(
    val id: String = "",
    val title: String = "",
    val note: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val createdAt: Long = 0L,
    val createdBy: String = ""
)


