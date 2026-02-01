package com.example.weather_app.domain.util

import java.text.SimpleDateFormat
import java.util.Locale

fun formatLastUpdate(time: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm, dd MMM", Locale.getDefault())

        val date = inputFormat.parse(time)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        time
    }
}

