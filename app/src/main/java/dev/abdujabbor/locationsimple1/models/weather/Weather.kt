package dev.abdujabbor.locationsimple1.models.weather

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)