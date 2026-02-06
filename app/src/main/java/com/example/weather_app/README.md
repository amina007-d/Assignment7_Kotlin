# Weather App Report

## Overview
This project is an Android Weather Application built with **Kotlin** and **Jetpack Compose**.  
The app allows users to search for a city and view current weather conditions and a **24-hour hourly forecast**.  
It supports offline mode using cached data and handles common network and API errors gracefully.

---

## Used APIs

The application uses public APIs from **Open-Meteo**.

### 1. Geocoding API (City Search)
- Base URL:  
  `https://geocoding-api.open-meteo.com/`
- Endpoint:  
  `/v1/search`
- Parameters:
    - `name` — city name (String)
    - `count` — number of results (default: 5)

Example request:
GET /v1/search?name=Astana&count=5

---

### 2. Weather Forecast API
- Base URL:  
  `https://api.open-meteo.com/`
- Endpoint:  
  `/v1/forecast`
- Parameters:
    - `latitude`
    - `longitude`
    - `current_weather=true`
    - `hourly=temperature_2m,relativehumidity_2m`
    - `timezone=auto`

Example request:
GET /v1/forecast?latitude=51.16&longitude=71.44&current_weather=true&hourly=temperature_2m,relativehumidity_2m&timezone=auto

---

## How to Run the App

1. Clone the repository:
   git clone <https://github.com/amina007-d/Assignment7_Kotlin>
2. Open the project in **Android Studio**.

3. Build and run the app on an emulator or physical device (API 24+).

4. Enter a city name in the search field to fetch weather data.

> No API key is required (Open-Meteo is free and open).

---

## Architecture Overview

The application follows **MVVM (Model–View–ViewModel)** architecture with the **Repository pattern**.

### Package structure:
- **data/**
    - `dto` – API interfaces and response models
    - `repository` – repository implementations
    - `local` – local cache (WeatherCache, SettingsStore)

- **domain/**
    - `model` – core data models (Weather, City)
    - `repository` – repository interfaces
    - `util` – helper functions (temperature conversion, date formatting)

- **presentation/**
    - `ui` – Jetpack Compose screens
    - `state` – UI state models
    - `ViewModels` – WeatherViewModel, SearchViewModel, SettingsViewModel

This ensures:
- clear separation of concerns
- maintainable and readable code
- testable business logic

---

## Caching Approach

- The last successful weather response is stored locally using `WeatherCache`.
- When a network request fails:
    - cached data is loaded instead of crashing
    - the UI shows a banner:
      Offline mode • showing cached data

Behavior:
- **Online:** API data is fetched and saved to cache
- **Offline:** cached data is displayed with proper labeling

---

## Error Handling Decisions

The application handles errors gracefully with user-friendly messages:

| Case | Message shown |
|------|---------------|
| Empty input | "Please enter a city name" |
| City not found (404) | "City not found" |
| Network timeout | "Network timeout. Please try again." |
| API rate limit (429) | "API rate limit exceeded" |
| Other server errors | "Server error" or "Unknown error" |

The UI never crashes and always provides feedback to the user.

---

## Last Update Time

The application displays the last update time of the weather data:

Example:
Last update: 22:15, 01 Feb

This helps users understand when the data was fetched from the API or loaded from cache.

---

## Known Limitations

- No background auto-refresh of weather data
- No list of favorite or saved cities
- Only 24-hour hourly forecast (no multi-day forecast)
- No localization (only English UI)
- Limited accessibility support (no screen reader optimization)

---
## Firebase Integration

The application is integrated with **Firebase Realtime Database** to store user-specific favorite cities.  
Firebase is used to demonstrate authentication, real-time data synchronization, CRUD operations, and basic security rules.

### Firebase Product
- **Firebase Realtime Database**
- **Firebase Authentication (Anonymous)**

Firestore is not used.

---

## Authentication

The app uses **Firebase Anonymous Authentication**.

- Authentication is initialized at app startup
- Each user is assigned a unique Firebase `uid`
- No registration or login UI is required
- Firebase-dependent logic is executed only after authentication is completed

This ensures that each user can securely access only their own data.

---

## Favorites Feature (Firebase)

Each authenticated user has a personal list of favorite cities stored in Firebase.

### Supported operations:
- Add a city to favorites
- Add or edit a short note for each city (e.g., *"Take umbrella"*)
- Delete a favorite city
- View favorites in real time
- *(Optional)* Display current weather for favorite cities using the existing Weather API

Favorites are updated automatically in the UI when data changes in Firebase, without requiring manual refresh.

---

## Data Model (Firebase)

Each favorite city stored in Firebase contains the following fields:

```json
{
  "id": "string",
  "title": "string",
  "note": "string",
  "lat": number,
  "lon": number,
  "createdAt": number,
  "createdBy": "uid"
}
```

## Summary

The Weather App:
- Fetches and displays correct weather data for a selected city
- Shows a 24-hour hourly forecast
- Uses MVVM + Repository architecture
- Supports offline mode with caching
- Handles errors gracefully
- Displays last update time
- Provides a clean and readable UI

This project satisfies all functional and technical requirements of the assignment.

