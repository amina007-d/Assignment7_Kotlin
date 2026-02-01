package com.example.weather_app.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weather_app.presentation.WeatherViewModel
import com.example.weather_app.ui.theme.backgroundBrush
import androidx.compose.ui.zIndex
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    unit: String
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(top = 100.dp)
    ) {

        // 🟡 OFFLINE BANNER
        if (state.isOffline) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E293B)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Offline mode • showing cached data",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }

            state.error != null -> {
                Text(
                    text = state.error!!,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.weather != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = if (state.isOffline) 32.dp else 0.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    WeatherContent(state = state, unit = unit)
                }
            }

            else -> EmptyState()
        }
    }
}



@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Search for a city",
            color = Color.White.copy(alpha = 0.7f)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Get weather anywhere in the world",
            color = Color.White.copy(alpha = 0.5f)
        )
    }
}

