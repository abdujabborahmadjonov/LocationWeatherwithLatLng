package dev.abdujabbor.locationsimple1.models

import androidx.room.Entity

@Entity(tableName = "users")
data class User(
    val lattitude: Double,
    val longtitude: Double
)