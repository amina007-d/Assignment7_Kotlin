package com.example.weather_app.presentation.ui
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.weather_app.presentation.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val unit by viewModel.unit.collectAsState()

    Column {
        Text("Temperature unit", color = Color.White)
        Row {
            RadioButton(
                selected = unit == "C",
                onClick = { viewModel.setUnit("C") },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.White,
                    unselectedColor = Color.LightGray
                )
            )
            Text("Celsius", color = Color.White)

            RadioButton(
                selected = unit == "F",
                onClick = { viewModel.setUnit("F") },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.White,
                    unselectedColor = Color.LightGray
                )
            )
            Text("Fahrenheit", color = Color.White)
        }
    }
}
