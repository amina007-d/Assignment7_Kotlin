package com.example.weather_app.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weather_app.presentation.FavoritesViewModel
import com.example.weather_app.domain.model.FavoriteCity
import com.example.weather_app.domain.model.Weather
import com.example.weather_app.ui.theme.backgroundBrush

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    weatherRepository: com.example.weather_app.domain.repository.WeatherRepository,
    onBack: () -> Unit
) {
    val favorites by viewModel.favorites.collectAsState()
    val weatherMap by viewModel.favoriteWeather.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var editedNote by remember { mutableStateOf("") }
    var selectedCityId by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(favorites) {
        viewModel.loadWeatherForFavorites(weatherRepository)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(12.dp)
    ) {
        Column {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Favorites",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(12.dp))

            if (favorites.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No favorite cities yet",
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(favorites) { city ->
                        FavoriteCityCard(
                            city = city,
                            weather = weatherMap[city.id],
                            onDelete = { viewModel.deleteFavorite(city.id) },
                            onEdit = {
                                selectedCityId = city.id
                                editedNote = city.note
                                showEditDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            confirmButton = {
                Button(onClick = {
                    if (editedNote.isBlank()) {
                        errorText = "Note cannot be empty"
                    } else {
                        viewModel.updateNote(selectedCityId, editedNote)
                        errorText = null
                        showEditDialog = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Edit note") },
            text = {
                Column {
                    TextField(
                        value = editedNote,
                        onValueChange = {
                            editedNote = it
                            errorText = null
                        },
                        placeholder = { Text("Enter note") }
                    )
                    errorText?.let {
                        Text(it, color = Color.Red)
                    }
                }
            }
        )
    }
}
@Composable
fun FavoriteCityCard(
    city: FavoriteCity,
    weather: Weather?,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                text = city.title,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            if (weather != null) {
                Text(
                    text = "${weather.temperature.toInt()}°",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall
                )
            } else {
                Text(
                    text = "Loading weather...",
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = city.note,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}
