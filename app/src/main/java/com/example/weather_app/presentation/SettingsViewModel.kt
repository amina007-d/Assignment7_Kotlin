package com.example.weather_app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.local.SettingsStore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val store: SettingsStore
) : ViewModel() {

    val unit: StateFlow<String> =
        store.unitFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            "C"
        )

    fun setUnit(unit: String) {
        viewModelScope.launch {
            store.setUnit(unit)
        }
    }
}
