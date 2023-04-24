package dev.abdujabbor.locationsimple1.models.weather

data class WeatherData(
    val weather: List<Weather>,
    val main: Main,
    val name: String
)