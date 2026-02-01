package com.example.weather_app.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.example.weather_app.domain.model.Weather
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore("weather_cache")

class WeatherCache(private val context: Context) {

    private val WEATHER_JSON = stringPreferencesKey("weather_json")

    private val gson = Gson()

    suspend fun save(weather: Weather) {
        context.dataStore.edit { prefs ->
            prefs[WEATHER_JSON] = gson.toJson(weather)
        }
    }

    suspend fun load(): Weather? {
        val prefs = context.dataStore.data.first()
        return prefs[WEATHER_JSON]?.let {
            gson.fromJson(it, Weather::class.java)
        }
    }
}
