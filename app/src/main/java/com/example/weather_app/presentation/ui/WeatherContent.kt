package com.example.weather_app.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.domain.util.cToF
import com.example.weather_app.domain.util.formatLastUpdate
import com.example.weather_app.presentation.FavoritesViewModel
import com.example.weather_app.presentation.state.WeatherUiState

@Composable
fun WeatherContent(
    state: WeatherUiState,
    unit: String,
    favoritesViewModel: FavoritesViewModel
) {
    val weather = state.weather ?: return

    var showDialog by remember { mutableStateOf(false) }
    var note by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = weather.city,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Text(
                    text = "Last update: ${formatLastUpdate(weather.lastUpdate)}",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }

            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Add to favorites",
                    tint = Color.White
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        val temp =
            if (unit == "F") cToF(weather.temperature) else weather.temperature

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${temp.toInt()}°",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = "Feels like ${weather.feelsLike.toInt()}°$unit",
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            WeatherStat("Wind", "${weather.windSpeed} km/h")
            WeatherStat("Humidity", "${weather.humidity}%")
        }

        Spacer(Modifier.height(20.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(weather.hourly) { hour ->
                val hourTemp =
                    if (unit == "F") cToF(hour.temperature) else hour.temperature

                HourlyCard(
                    time = hour.time.takeLast(5),
                    temp = hourTemp
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                errorText = null
            },
            confirmButton = {
                Button(onClick = {
                    if (note.isBlank()) {
                        errorText = "Note cannot be empty"
                    } else {
                        favoritesViewModel.addFavorite(
                            title = weather.city,
                            note = note,
                            lat = weather.lat,
                            lon = weather.lon
                        )
                        note = ""
                        errorText = null
                        showDialog = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    errorText = null
                }) {
                    Text("Cancel")
                }
            },
            title = {
                Text("Add note for ${weather.city}")
            },
            text = {
                Column {
                    TextField(
                        value = note,
                        onValueChange = {
                            note = it
                            errorText = null
                        },
                        placeholder = { Text("Enter note") }
                    )

                    errorText?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = it,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun WeatherStat(title: String, value: String) {
    Column {
        Text(text = title, color = Color.LightGray)
        Text(text = value, color = Color.White, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun HourlyCard(time: String, temp: Double) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier.size(width = 72.dp, height = 100.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = time, color = Color.LightGray)
            Text(
                text = "${temp.toInt()}°",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.18f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp), content = content)
    }
}
