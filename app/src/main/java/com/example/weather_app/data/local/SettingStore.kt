package com.example.weather_app.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore("settings")

class SettingsStore(context: Context) {

    private val dataStore = context.settingsDataStore
    private val UNIT = stringPreferencesKey("unit")

    val unitFlow: Flow<String> =
        dataStore.data.map { it[UNIT] ?: "C" }

    suspend fun setUnit(unit: String) {
        dataStore.edit {
            it[UNIT] = unit
        }
    }
}
